package atomspace.performance.storage.janusgraph;

import atomspace.performance.DBAtom;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleAtomEvaluationJanusGraphModel extends TripleAtomJanusGraphModel {


    public TripleAtomEvaluationJanusGraphModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomEvaluationJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Evaluation";
    }

    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(EVALUATION_LINK,
                atomspace.getNode(PREDICATE_NODE, triple.predicate),
                atomspace.getLink(LIST_LINK,
                        atomspace.getNode(SUBJECT_NODE, triple.subject),
                        atomspace.getNode(OBJECT_NODE, triple.object)));
    }

    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

        GraphTraversalSource g = storage.graph.traversal();

        for (int i = 0; i < iterations; i++) {
            Triple triple = triples.get(rand.nextInt(size));

            // Find ListLink vertex
            GraphTraversal<Vertex, Vertex> listLinkTraversal = g.V()
                    .has("type", SUBJECT_NODE)
                    .has("value", triple.subject)
                    .out(getArgType(0))
                    .hasLabel(LIST_LINK);


            while (listLinkTraversal.hasNext()) {

                Vertex listLink = listLinkTraversal.next();
                // Find PredicateNode vertex
                GraphTraversal<Vertex, Vertex> predicateNodeTraversal = g.V(listLink.id())
                        .out(getArgType(1))
                        .hasLabel(EVALUATION_LINK)
                        .in(getArgType(0))
                        .hasLabel(PREDICATE_NODE)
                        .property("value", triple.predicate);

                while (predicateNodeTraversal.hasNext()) {

                    // next() method must to be called to allow further traversing
                    predicateNodeTraversal.next();

                    // Find ObjectNode vertex
                    GraphTraversal<Vertex, Vertex> objectNodeTraversal = g.V(listLink.id())
                            .in(getArgType(1))
                            .hasLabel(OBJECT_NODE);

                    while (objectNodeTraversal.hasNext()) {
                        String object = objectNodeTraversal.next().value("value").toString();
                        objects.add(object);
                    }
                }
            }
        }

        return objects;
    }
}
