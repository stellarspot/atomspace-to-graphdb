package atomspace.performance.storage.janusgraph;

import atomspace.performance.DBAtom;
import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.storage.neo4japi.TripleAtomNeo4jAPIModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleAtomPredicateJanusGraphModel extends TripleAtomJanusGraphModel{

    public TripleAtomPredicateJanusGraphModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomPredicateJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Predicate";
    }

    // PredicateLink(SubjectNode, ObjectNode)
    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(toLinkLabel(triple.predicate),
                atomspace.getNode("SubjectNode", triple.subject),
                atomspace.getNode("ObjectNode", triple.object));

    }

    // PredicateLink(*, ObjectNode)
    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);


//        GraphDatabaseService graph = storage.graph;
//        try (Transaction tx = graph.beginTx()) {
//
//            for (int i = 0; i < iterations; i++) {
//
//                Triple triple = triples.get(rand.nextInt(size));
//
//                Node subjectNode = graph.findNode(SUBJECT_LABEL, "value", triple.subject);
//
//                for (Relationship r1 : subjectNode.getRelationships(getArgType(0), Direction.INCOMING)) {
//
//                    Node predicateNode = r1.getStartNode();
//
//                    Label predicateLabel = Label.label(toLinkLabel(triple.predicate));
//                    if (!predicateNode.hasLabel(predicateLabel)) {
//                        continue;
//                    }
//
//                    Relationship r2 = predicateNode.getSingleRelationship(getArgType(1), Direction.OUTGOING);
//
//                    Node objectNode = r2.getEndNode();
//
//                    if (objectNode.hasLabel(OBJECT_LABEL)) {
//                        String object = objectNode.getProperty("value").toString();
//                        objects.add(object);
//                    }
//                }
//            }
//            tx.success();
//        }

        return objects;
    }

    private static Relationship getRelationship(Node node, RelationshipType type, Direction d, String key, Object value) {
        for (Relationship r : node.getRelationships(type, d)) {
            if (r.hasProperty(key) && value.equals(r.getProperty(key))) {
                return r;
            }
        }
        return null;
    }

    private static String toLinkLabel(String name) {
        return String.format("%s_LINK", name.toUpperCase());
    }
}
