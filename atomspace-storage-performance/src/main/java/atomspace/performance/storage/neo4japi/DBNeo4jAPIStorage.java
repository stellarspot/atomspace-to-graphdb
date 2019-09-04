package atomspace.performance.storage.neo4japi;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class DBNeo4jAPIStorage implements Closeable {


    final File databaseDirectory;
    final GraphDatabaseService graph;


    public DBNeo4jAPIStorage() {
        this("/tmp/neo4j/data-local-perf");
    }

    public DBNeo4jAPIStorage(String databaseDirectory) {
        this.databaseDirectory = new File(databaseDirectory);
        graph = new GraphDatabaseFactory().newEmbeddedDatabase(this.databaseDirectory);
    }

    public Node getOrCreate(Label label, String key, Object value) {

        Node node = graph.findNode(label, key, value);
        if (node == null) {
            node = graph.createNode();
            node.addLabel(label);
            node.setProperty(key, value);
        }
        return node;
    }

    public void dump() {
        System.out.printf("--- Dump ---%n");
        try (Transaction tx = graph.beginTx()) {

//            for (Node n : graph.getAllNodes()) {
//                System.out.println(toString(n));
//            }


            for (Relationship r : graph.getAllRelationships()) {
                System.out.println(toString(r));
            }

            tx.success();
        }
        System.out.printf("--- ---- ---%n");
    }

    public void clearDB() {
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

    @Override
    public void close() throws IOException {
        graph.shutdown();
    }

    public static String toString(Node node) {

        String type = "Unknown";

        for (Label label : node.getLabels()) {
            type = label.name();
            if (type.equals("Atom")) {
                continue;
            }
            break;
        }

        if (node.hasProperty("value")) {
            String value = node.getProperty("value").toString();
            return String.format("%s('%s')", type, value);
        }

        if (node.hasProperty("id")) {
            String value = node.getProperty("id").toString();
            return String.format("%s('%s')", type, value);
        }

        return String.format("%s()", type);
    }

    public static String toString(Relationship relationship) {
        String type = relationship.getType().name();
        String start = toString(relationship.getStartNode());
        String end = toString(relationship.getEndNode());

        if (relationship.hasProperty("position")) {
            String position = relationship.getProperty("position").toString();
            return String.format("(%s) - [%s {position: '%s'}] -> (%s)", start, type, position, end);
        }

        return String.format("(%s) - [%s] -> (%s)", start, type, end);
    }

}
