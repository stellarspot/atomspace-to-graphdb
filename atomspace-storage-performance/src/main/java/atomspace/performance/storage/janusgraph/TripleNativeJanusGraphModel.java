package atomspace.performance.storage.janusgraph;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleNativeJanusGraphModel extends TripleJanusGraphModel {

    public TripleNativeJanusGraphModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleNativeJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Native";
    }

    @Override
    public void storeTriples() {

        GraphTraversalSource g = storage.graph.traversal();

        try (Transaction tx = g.tx()) {

            for (Triple triple : tripleGraph.getTriples()) {

                Vertex sbj = storage.getOrCreate(g, SUBJECT_LABEL, "value", triple.subject);
                Vertex obj = storage.getOrCreate(g, OBJECT_LABEL, "value", triple.object);
                sbj.addEdge(triple.predicate, obj);
            }
            tx.commit();
        }
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

            GraphTraversal<Vertex, Vertex> iter = g.V()
                    .hasLabel(SUBJECT_LABEL)
                    .has("value", triple.subject)
                    .out(triple.predicate).V().hasLabel(OBJECT_LABEL);

            if (iter.hasNext()) {
                objects.add(iter.next().value("value"));
            }
        }

        return objects;
    }
}
