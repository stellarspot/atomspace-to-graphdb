package sample.neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;

public class Neo4jAPIExample {

    public static void main(String... args) throws Exception {

        File databaseDirectory = new File("/tmp/neo4j/data-local");
        FileUtils.deleteRecursively(databaseDirectory);
        GraphDatabaseService graph = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDirectory);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> graph.shutdown()));

        addPredicate(graph, "Alice", "likes", "ice-cream");

        printPredicates(graph);

        clearGraph(graph);
        printPredicates(graph);
    }

    private static void addPredicate(GraphDatabaseService graph, String subject, String predicate, String object) {
        try (Transaction tx = graph.beginTx()) {

            Node subjectNode = graph.createNode();
            subjectNode.addLabel(Label.label("SubjectNode"));
            subjectNode.setProperty("value", subject);

            Node objectNode = graph.createNode();
            objectNode.addLabel(Label.label("ObjectNode"));
            objectNode.setProperty("value", object);

            RelationshipType predicateRelation = RelTypes.valueOf(predicate.toUpperCase());
            Relationship relationship = subjectNode.createRelationshipTo(objectNode, predicateRelation);

            tx.success();
        }
    }

    private static void printPredicates(GraphDatabaseService graph) {
        try (Transaction tx = graph.beginTx()) {

            for (Relationship r : graph.getAllRelationships()) {
                System.out.println(toString(r));
            }

            tx.success();
        }
    }

    private static String toString(Node node) {

        String type = "Unknown";

        for (Label label : node.getLabels()) {
            type = label.name();
            break;
        }

        String value = node.getProperty("value").toString();

        return String.format("%s('%s')", type, value);
    }

    private static String toString(Relationship relationship) {
        String type = relationship.getType().name();
        String start = toString(relationship.getStartNode());
        String end = toString(relationship.getEndNode());
        return String.format("%s(%s,%s)", type, start, end);
    }

    private static void clearGraph(GraphDatabaseService graph) {
        try (Transaction tx = graph.beginTx()) {
            for (Relationship r : graph.getAllRelationships()) {
                r.delete();
            }
            for (Node n : graph.getAllNodes()) {
                n.delete();
            }
            tx.success();
        }
    }

    private enum RelTypes implements RelationshipType {
        LIKES
    }
}