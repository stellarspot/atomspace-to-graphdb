package sample.performance;

import atomspace.performance.generator.FixedTripleGraph;
import atomspace.performance.generator.RandomTripleGraph;
import atomspace.performance.generator.Triple;
import atomspace.performance.generator.TripleGraph;
import atomspace.performance.storage.neo4j.*;

import java.util.List;

public class PerformanceSampleNeo4j {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {

            storage.clearDB();

//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"));

//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"),
//                    new Triple("Alice", "likes", "apple"),
//                    new Triple("Bob", "likes", "apple")
//            );

            TripleGraph tripleGraph = new RandomTripleGraph();

            TripleNeo4jModel nativeModel = new TripleNativeNeo4jModel(storage, tripleGraph);
            TripleNeo4jModel predicateModel = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
            TripleNeo4jModel evaluationModel = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);

            measureRequests(5, tripleGraph, nativeModel, predicateModel, evaluationModel);
        }
    }

    private static void measureRequests(int iterations, TripleGraph tripleGraph, TripleNeo4jModel... models) {
        System.out.printf("triples: %s%n", tripleGraph.getStatistics());
        System.out.printf("iterations: %d%n", iterations);

        for (int i = 0; i < iterations; i++) {
            for (TripleNeo4jModel model : models) {
                System.out.printf("Model: %s%n", model.getName());
                model.storeTriples();
                List<String> objects = model.queryObject();
                System.out.printf("objects: %d%n", objects.size());
//                for (String obj : objects) {
//                    System.out.printf("Object: %s%n", obj);
//                }
            }
        }
    }
}
