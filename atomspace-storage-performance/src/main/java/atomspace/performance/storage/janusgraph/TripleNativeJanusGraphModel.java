package atomspace.performance.storage.janusgraph;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.janusgraph.core.JanusGraphEdge;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.neo4j.graphdb.Label;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleNativeJanusGraphModel extends TripleJanusGraphModel {

    private static final String SUBJECT_LABEL = "SubjectNode";
    private static final String OBJECT_LABEL = "ObjectNode";

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

        JanusGraphTransaction tx = storage.graph.newTransaction();

        for (Triple triple : tripleGraph.getTriples()) {

            JanusGraphVertex sbj = tx.addVertex(SUBJECT_LABEL);
            sbj.property("value", triple.subject);

            JanusGraphVertex obj = tx.addVertex(OBJECT_LABEL);
            obj.property("value", triple.object);

            JanusGraphEdge edge = obj.addEdge(triple.predicate.toUpperCase(), sbj);
        }

        tx.commit();
    }

    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

//        GraphDatabaseService graph = storage.graph;
//        try (Transaction tx = graph.beginTx()) {
//            for (int i = 0; i < iterations; i++) {
//                Triple triple = triples.get(rand.nextInt(size));
//
//                Node node = graph.findNode(SUBJECT_LABEL, "value", triple.subject);
//
//                RelationshipType linkType = RelationshipType.withName(triple.predicate);
//                for (Relationship r : node.getRelationships(linkType, Direction.OUTGOING)) {
//                    String object = r.getEndNode().getProperty("value").toString();
//                    objects.add(object);
//                }
//            }
//
//            tx.success();
//        }

        return objects;
    }
}
