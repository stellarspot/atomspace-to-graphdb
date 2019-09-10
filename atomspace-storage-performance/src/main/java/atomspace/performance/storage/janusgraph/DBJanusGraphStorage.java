package atomspace.performance.storage.janusgraph;

import atomspace.performance.storage.DBStorage;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.io.IOException;

public class DBJanusGraphStorage implements DBStorage {


    final JanusGraph graph;


    public DBJanusGraphStorage() {
        this("/tmp/janusgraph/data-local-perf");
    }

    public DBJanusGraphStorage(String databaseDirectory) {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", databaseDirectory)
                .open();
    }

    @Override
    public void clearDB() {
        GraphTraversalSource g = graph.traversal();
        g.V().drop().iterate();
        g.tx().commit();
    }

    @Override
    public void close() throws IOException {
        graph.close();
    }

    public void dump() {
        System.out.printf("--- Dump ---%n");
        GraphTraversalSource g = graph.traversal();

        GraphTraversal<Edge, Edge> edges = g.E();
        while (edges.hasNext()) {
            Edge edge = edges.next();

            String startLabel = edge.inVertex().label();
            String startValue = edge.inVertex().value("value");

            String endLabel = edge.outVertex().label();
            String endValue = edge.outVertex().value("value");

            String edgeLabel = edge.label();
            System.out.printf("(:%s {value: '%s'}) - [%s] -> (:%s {value: '%s'})%n",
                    startLabel, startValue, edgeLabel, endLabel, endValue);
        }
        System.out.printf("--- ---- ---%n");
    }

    Vertex getOrCreate(GraphTraversalSource g, String label, String key, String value) {

        GraphTraversal<Vertex, Vertex> t = g.V().hasLabel(label).has(key, value);
        return t.hasNext() ? t.next() : g.addV(label).property(key, value).next();
    }
}
