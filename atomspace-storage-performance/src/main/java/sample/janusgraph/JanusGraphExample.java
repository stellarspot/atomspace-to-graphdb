package sample.janusgraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

public class JanusGraphExample {

    public static final String CONFIG_FILE = "conf/janusgraph.properties";

    public static void main(String[] args) {
        JanusGraph graph = JanusGraphFactory.open(CONFIG_FILE);
        GraphTraversalSource g = graph.traversal();

        System.out.printf("Vertices count: %s%n", g.V().count().next());
        System.out.printf("labels: %s%n", g.V().label().next());

        System.exit(0);
    }

    private static void addPredicate(JanusGraph graph, String subject, String predicate, String object) {

        GraphTraversalSource g = graph.traversal();

        g
                .addV("Subject").property("value", subject)
                .addV("Object").property("value", object);

    }
}
