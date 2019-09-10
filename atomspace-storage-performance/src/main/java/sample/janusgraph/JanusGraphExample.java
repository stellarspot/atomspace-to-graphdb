package sample.janusgraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;

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

    private static final String SUBJECT_LABEL = "SubjectNode";
    private static final String OBJECT_LABEL = "ObjectNode";
    private static final String PREDICATE_LABEL = "PREDICATE";

    private static void addPredicate(JanusGraph graph, String subject, String predicate, String object) {

        JanusGraphManagement management = graph.openManagement();

        if (!management.containsVertexLabel(SUBJECT_LABEL)) {
            management.makeVertexLabel(SUBJECT_LABEL).make();
        }

        if (!management.containsVertexLabel(OBJECT_LABEL)) {
            management.makeVertexLabel(OBJECT_LABEL).make();
        }

        if (!management.containsEdgeLabel(PREDICATE_LABEL)) {
            management.makeEdgeLabel(PREDICATE_LABEL).make();
        }

        management.commit();

        JanusGraphTransaction tx = graph.newTransaction();


        JanusGraphVertex sbj = tx.addVertex(SUBJECT_LABEL);
        sbj.property("value", subject);

        JanusGraphVertex obj = tx.addVertex(OBJECT_LABEL);
        obj.property("value", object);


        JanusGraphEdge edge = sbj.addEdge(predicate.toUpperCase(), obj);

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

        GraphTraversal<Edge, Edge> edges = g.E();
        while (edges.hasNext()) {
            Edge edge = edges.next();
            System.out.printf("edge: %s%n", edge);
            String startLabel = edge.inVertex().label();
            String startValue = edge.inVertex().value("value");
            String endLabel = edge.outVertex().label();
            String endValue = edge.outVertex().value("value");
            String edgeLabel = edge.label();
            System.out.printf("(:%s {value: '%s'}) - [%s] -> (:%s {value: '%s'})%n",
                    startLabel, startValue, edgeLabel, endLabel, endValue);
        }
        System.out.printf("--- ---------- ---%n");
    }

}
