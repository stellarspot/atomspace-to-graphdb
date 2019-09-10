package atomspace.performance.storage.janusgraph;

import atomspace.performance.storage.DBStorage;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

import java.io.IOException;
import java.util.Iterator;

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
    public void close() {
        graph.close();
    }

    Vertex getOrCreate(GraphTraversalSource g, String label, String key, String value) {
        GraphTraversal<Vertex, Vertex> t = g.V().hasLabel(label).has(key, value);
        return t.hasNext() ? t.next() : g.addV(label).property(key, value).next();
    }

    public void dump() {
        System.out.printf("--- Dump ---%n");
        GraphTraversalSource g = graph.traversal();

        GraphTraversal<Edge, Edge> edges = g.E();
        while (edges.hasNext()) {
            Edge edge = edges.next();

            String startVertex = toString(edge.inVertex());
            String endVertex = toString(edge.outVertex());
            String edgeLabel = edge.label();

            System.out.printf("%s - [%s] -> %s%n", startVertex, edgeLabel, endVertex);
        }
        System.out.printf("--- ---- ---%n");
    }

    private static String toString(Vertex v) {

        StringBuilder builder = new StringBuilder();

        String label = v.label();
        builder.append("(:").append(label);

        Iterator<VertexProperty<Object>> props = v.properties();

        builder.append(" {");

        while (props.hasNext()) {
            VertexProperty<Object> prop = props.next();
            String key = prop.key();
            Object value = v.value(key);
            builder.append(key).append(": '").append(value).append("'");
        }

        builder.append("})");
        return builder.toString();
    }
}
