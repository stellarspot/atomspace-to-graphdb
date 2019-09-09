package sample.janusgraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.Map;

public class JanusGraphExample {

    public static void main(String[] args) {

        JanusGraph graph = JanusGraphFactory.build()
                .set("storage.backend", "berkeleyje")
                .set("storage.directory", "/tmp/janusgraph/sample")
                .open();


        addPredicate(graph, "s1", "p1", "o1");

//        System.out.printf("Vertices count: %s%n", g.V().count().next());
//        System.out.printf("labels: %s%n", g.V().label().next());

        print(graph);
        clearDB(graph);
        print(graph);

        System.exit(0);
    }

    private static void sample(JanusGraph graph) {
        JanusGraphTransaction tx = graph.newTransaction();
        // vertices

        Vertex saturn = tx.addVertex("type", "titan", "name", "saturn", "age", 10000);
        tx.commit();
    }

    private static void addPredicate(JanusGraph graph, String subject, String predicate, String object) {

        JanusGraphTransaction tx = graph.newTransaction();
        tx.addVertex("Subject").property("value", subject);
        tx.addVertex("Object").property("value", object);
        tx.commit();
    }

    private static void clearDB(JanusGraph graph) {
        GraphTraversalSource g = graph.traversal();
        g.V().drop().iterate();
        g.tx().commit();
    }

    private static void print(JanusGraph graph) {
        System.out.printf("--- dump graph ---%n");
        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Map<Object, Object>> properties = g.V().valueMap();

        while (properties.hasNext()) {
            System.out.printf("values: %s%n", properties.next());
        }
        System.out.printf("--- ---------- ---%n");
    }

}
