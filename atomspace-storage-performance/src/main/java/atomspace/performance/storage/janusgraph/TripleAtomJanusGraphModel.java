package atomspace.performance.storage.janusgraph;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

public abstract class TripleAtomJanusGraphModel extends TripleJanusGraphModel {

    protected static final String ARG_TYPE = "ARG";

    protected final DBAtomSpace atomspace = new DBAtomSpace();

    public TripleAtomJanusGraphModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    protected abstract DBAtom toAtom(Triple triple);

    @Override
    public void storeTriples() {
        GraphTraversalSource g = storage.graph.traversal();

        try (Transaction tx = g.tx()) {

            for (Triple triple : tripleGraph.getTriples()) {
                putAtom(g, toAtom(triple));
            }

            tx.commit();
        }
    }

    protected LookupNode putAtom(GraphTraversalSource g, DBAtom atom) {

        if (atom instanceof DBNode) {
            return putNode(g, (DBNode) atom);
        } else if (atom instanceof DBLink) {
            return putLink(g, (DBLink) atom);
        }

        throw new RuntimeException("Unknown atom type!");
    }

    protected LookupNode putNode(GraphTraversalSource g, DBNode node) {

        GraphTraversal<Vertex, Vertex> t = g.V()
                .hasLabel(node.type)
                .has("id", node.id);

//        System.out.printf("node type: %s%n", node.type);

        if (t.hasNext()) {
            return new LookupNode(true, t.next());
        }

        Vertex vertex = g
                .addV(node.type)
                .property("id", node.id)
                .property("value", node.value)
                .next();

        return new LookupNode(true, vertex);

    }

    protected LookupNode putLink(GraphTraversalSource g, DBLink link) {


        GraphTraversal<Vertex, Vertex> t = g.V()
                .hasLabel(link.type)
                .has("id", link.id);

//        System.out.printf("link type: %s%n", link.type);

        if (t.hasNext()) {
            return new LookupNode(true, t.next());
        }

        Vertex vertex = g
                .addV(link.type)
                .property("id", link.id)
                .next();

        for (int i = 0; i < link.atoms.length; i++) {
            DBAtom atom = link.atoms[i];
            Vertex child = putAtom(g, atom).vertex;
            child.addEdge(getArgType(i), vertex);
        }

        return new LookupNode(false, vertex);
    }

    protected static String getArgType(int position) {
        return String.format("%s_%d", ARG_TYPE, position);

    }

    static class LookupNode {
        final boolean existed;
        final Vertex vertex;

        public LookupNode(boolean existed, Vertex vertex) {
            this.existed = existed;
            this.vertex = vertex;
        }
    }

}
