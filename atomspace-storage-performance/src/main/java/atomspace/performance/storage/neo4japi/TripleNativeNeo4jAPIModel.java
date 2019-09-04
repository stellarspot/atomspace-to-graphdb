package atomspace.performance.storage.neo4japi;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.*;

import java.util.*;

public class TripleNativeNeo4jAPIModel extends TripleNeo4jAPIModel {

    public TripleNativeNeo4jAPIModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleNativeNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Native";
    }

    @Override
    public void storeTriples() {

        GraphDatabaseService graph = storage.graph;
        try (Transaction tx = graph.beginTx()) {

            for (Triple triple : tripleGraph.getTriples()) {

                Node subjectNode = storage.getOrCreate(SUBJECT_LABEL, "value", triple.subject);
                Node objectNode = storage.getOrCreate(OBJECT_LABEL, "value", triple.object);

                RelationshipType linkType = RelationshipType.withName(triple.predicate);
                Relationship relationship = subjectNode.createRelationshipTo(objectNode, linkType);
            }

            tx.success();
        }
    }

    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

        GraphDatabaseService graph = storage.graph;
        try (Transaction tx = graph.beginTx()) {
            for (int i = 0; i < iterations; i++) {
                Triple triple = triples.get(rand.nextInt(size));

                Node node = graph.findNode(SUBJECT_LABEL, "value", triple.subject);

                RelationshipType linkType = RelationshipType.withName(triple.predicate);
                for (Relationship r : node.getRelationships(linkType, Direction.OUTGOING)) {
                    String object = r.getEndNode().getProperty("value").toString();
                    objects.add(object);
                }
            }

            tx.success();
        }

        return objects;
    }
}
