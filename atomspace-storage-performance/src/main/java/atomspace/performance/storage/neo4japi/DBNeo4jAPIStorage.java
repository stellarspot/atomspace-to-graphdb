package atomspace.performance.storage.neo4japi;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

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


    public void dump() {
        try (Transaction tx = graph.beginTx()) {

            for (Relationship r : graph.getAllRelationships()) {
                System.out.println(toString(r));
            }

            tx.success();
        }
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

}