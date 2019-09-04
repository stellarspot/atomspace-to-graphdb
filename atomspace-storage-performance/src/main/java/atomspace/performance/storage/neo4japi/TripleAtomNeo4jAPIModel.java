package atomspace.performance.storage.neo4japi;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.*;

import java.util.Iterator;

public abstract class TripleAtomNeo4jAPIModel extends TripleNeo4jAPIModel {

    protected static final Label ATOM_LABEL = Label.label("Atom");
    protected static final Label NODE_LABEL = Label.label("Node");
    protected static final Label LINK_LABEL = Label.label("Link");
    protected static final String ARG_TYPE = "ARG";

    protected final DBAtomSpace atomspace = new DBAtomSpace();

    public TripleAtomNeo4jAPIModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    protected abstract DBAtom toAtom(Triple triple);

    @Override
    public void storeTriples() {
        GraphDatabaseService graph = storage.graph;
        try (org.neo4j.graphdb.Transaction tx = graph.beginTx()) {

            for (Triple triple : tripleGraph.getTriples()) {
                putAtom(toAtom(triple));
            }

            tx.success();
        }
    }

    protected LookupNode putAtom(DBAtom atom) {

        if (atom instanceof DBNode) {
            return putNode((DBNode) atom);
        } else if (atom instanceof DBLink) {
            return putLink((DBLink) atom);
        }

        throw new RuntimeException("Unknown aom type!");
    }

    protected LookupNode putNode(DBNode node) {

        GraphDatabaseService graph = storage.graph;
        Label nodeLabel = Label.label(node.type);
        Node n = graph.findNode(nodeLabel, "id", node.id);
        if (n == null) {
            n = graph.createNode();
            n.addLabel(ATOM_LABEL);
            n.addLabel(nodeLabel);
            n.setProperty("id", node.id);
            n.setProperty("value", node.value);
            return new LookupNode(false, n);
        }

        return new LookupNode(true, n);
    }

    protected LookupNode putLink(DBLink link) {

        GraphDatabaseService graph = storage.graph;

        Label linkLabel = Label.label(link.type);
        Node n = graph.findNode(linkLabel, "id", link.id);

        if (n != null) {
            return new LookupNode(true, n);
        }

        n = graph.createNode();
        n.addLabel(ATOM_LABEL);
        n.addLabel(linkLabel);
        n.setProperty("id", link.id);


        for (int i = 0; i < link.atoms.length; i++) {
            DBAtom atom = link.atoms[i];
            Node child = putAtom(atom).node;
            Relationship relationship = n.createRelationshipTo(child, getArgType(i));
        }

        return new LookupNode(false, n);
    }

    protected static RelationshipType getArgType(int position) {
        return RelationshipType.withName(String.format("%s_%d", ARG_TYPE, position));

    }

    static class LookupNode {
        final boolean existed;
        final Node node;

        public LookupNode(boolean existed, Node node) {
            this.existed = existed;
            this.node = node;
        }
    }
}
