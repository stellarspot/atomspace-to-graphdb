package atomspace.performance.storage.neo4japi;

import atomspace.performance.DBAtom;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.*;

import java.util.*;

public class TripleAtomEvaluationNeo4jAPIModel extends TripleAtomNeo4jAPIModel {


    private static final String EVALUAION_LINK = "Evaluation";
    private static final String LIST_LINK = "List";
    private static final String PREDICATE_NODE = "PredicateNode";
    private static final String SUBJECT_NODE = "SubjectNode";
    private static final String OBJECT_NODE = "ObjectNode";


    public TripleAtomEvaluationNeo4jAPIModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomEvaluationNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Evaluation";
    }

    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(EVALUAION_LINK,
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


        GraphDatabaseService graph = storage.graph;
        try (Transaction tx = graph.beginTx()) {

            for (int i = 0; i < iterations; i++) {

                Triple triple = triples.get(rand.nextInt(size));

                Node subjectNode = graph.findNode(SUBJECT_LABEL, "value", triple.subject);

                for (Relationship r1 : subjectNode.getRelationships(getArgType(0), Direction.INCOMING)) {

                    Node listNode = r1.getStartNode();

                    for (Relationship r3 : listNode.getRelationships(getArgType(1), Direction.INCOMING)) {
                        Node evaluationNode = r3.getStartNode();

                        Relationship r4 = evaluationNode.getSingleRelationship(getArgType(0), Direction.OUTGOING);
                        Node predicateNode = r4.getEndNode();

                        if (predicateNode.hasLabel(Label.label(PREDICATE_NODE))
                                && triple.predicate.equals(predicateNode.getProperty("value"))) {

                            Relationship r2 = listNode.getSingleRelationship(getArgType(1), Direction.OUTGOING);
                            Node objectNode = r2.getEndNode();
                            if (objectNode.hasLabel(Label.label(OBJECT_NODE))) {
                                String object = objectNode.getProperty("value").toString();
                                objects.add(object);

                            }
                        }
                    }
                }
            }
            tx.success();
        }

        return objects;
    }


    protected void dump() {
        GraphDatabaseService graph = storage.graph;
        try (Transaction tx = graph.beginTx()) {

            Iterator<Node> iter = graph.findNodes(Label.label(EVALUAION_LINK));

            while (iter.hasNext()) {
                System.out.printf("---%n");
                Node evaluationNode = iter.next();
                System.out.printf("evaluation: %s%n", DBNeo4jAPIStorage.toString(evaluationNode));

                for (Relationship r1 : evaluationNode.getRelationships(RelationshipType.withName("ARG_0"))) {
                    Node predicateNode = r1.getEndNode();
                    System.out.printf("predicate: %s%n", DBNeo4jAPIStorage.toString(predicateNode));
                }

                for (Relationship r2 : evaluationNode.getRelationships(RelationshipType.withName("ARG_1"))) {
                    Node listNode = r2.getEndNode();
                    System.out.printf("list: %s%n", DBNeo4jAPIStorage.toString(listNode));

                    for (Relationship r3 : listNode.getRelationships(RelationshipType.withName("ARG_0"))) {
                        Node subjectNode = r3.getEndNode();
                        System.out.printf("subject: %s%n", DBNeo4jAPIStorage.toString(subjectNode));
                    }

                    int i = 0;
                    for (Relationship r4 : listNode.getRelationships(RelationshipType.withName("ARG_1"))) {
                        Node objectNode = r4.getEndNode();
                        System.out.printf("object[%d]: %s%n", i++, DBNeo4jAPIStorage.toString(objectNode));
                    }
                }

            }

            System.out.println();

            tx.success();
        }
    }

    private static String toLinkLabel(String name) {
        return String.format("%s_LINK", name.toUpperCase());
    }
}
