package atomspace.performance.storage.janusgraph;

import atomspace.performance.storage.DBStorage;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.Iterator;

public class DBJanusGraphStorage implements DBStorage {

    static final String BY_SUBJECT_INDEX = "bySubjectLabel";
    static final String BY_OBJECT_INDEX = "byObjectLabel";
    static final String BY_TYPE_INDEX = "byTypeValueProperty";
    static final String BY_TYPE_VALUE_INDEX = "byTypeValueProperty";
    static final String BY_ID_INDEX = "byIdProperty";

    final JanusGraph graph;


    public DBJanusGraphStorage() {
        this("/tmp/janusgraph/data-local-perf");
    }

    public DBJanusGraphStorage(String databaseDirectory) {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", String.format("%s/graph", databaseDirectory))
                .set("index.search.backend", "lucene")
                .set("index.search.directory", String.format("%s/index", databaseDirectory))
//                .set("query.force-index", true)
                .open();


        makeIndices();
    }

    private void makeIndices() {
        // make indices
        JanusGraphManagement mgmt = graph.openManagement();

        createKeyIndex(mgmt, "byType", "type");
        createKeyIndex(mgmt, "byTypeAndId", "type", "id");
        createKeyIndex(mgmt, "byTypeAndValue", "type", "value");

        for (JanusGraphIndex ind : mgmt.getGraphIndexes(Vertex.class)) {
            System.out.printf("vertex index: %s%n", ind);
        }

        mgmt.commit();
    }

    private static void createKeyIndex(JanusGraphManagement mgmt, String indexName, String... keys) {
        if (mgmt.getGraphIndex(indexName) == null) {
            JanusGraphManagement.IndexBuilder builder = mgmt.buildIndex(indexName, Vertex.class);

            for (String key : keys) {
                builder = builder.addKey(mgmt.getOrCreatePropertyKey(key));
            }
            builder.buildCompositeIndex();
        }
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
        GraphTraversal<Vertex, Vertex> t = g.V().has("type", label).has(key, value);
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
