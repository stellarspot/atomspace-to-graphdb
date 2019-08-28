package sample.performance;

import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.TripleGraph;
import atomspace.performance.storage.neo4j.*;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceSampleNeo4j {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {


//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"));

//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"),
//                    new Triple("Alice", "likes", "apple"),
//                    new Triple("Bob", "likes", "apple")
//            );

            int N = 40;
            int subjectsNumber = N;
            int predicatesNumber = N / 2;
            int objectsNumber = N;
            int predicatesPerSubjectNumber = N / 4;

            TripleGraph tripleGraph = new RandomTripleGraph(
                    subjectsNumber,
                    predicatesNumber,
                    objectsNumber,
                    predicatesPerSubjectNumber);

            TripleNeo4jModel nativeModel = new TripleNativeNeo4jModel(storage, tripleGraph);
            TripleNeo4jModel predicateModel = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
            TripleNeo4jModel evaluationModel = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);

            measureRequests(storage, tripleGraph, 10, nativeModel, predicateModel, evaluationModel);
        }
    }

    private static void measureRequests(DBNeo4jStorage storage,
                                        TripleGraph tripleGraph,
                                        int iterations,
                                        TripleNeo4jModel... models) {
        System.out.printf("triples: %s%n", tripleGraph.getStatistics());
        System.out.printf("iterations: %d%n", iterations);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;


        for (TripleNeo4jModel model : models) {
            System.out.printf("Model: %s%n", model.getName());

            StopWatch createWatch = new StopWatch();
            createWatch.reset();
            createWatch.start();
            createWatch.suspend();

            StopWatch queryWatch = new StopWatch();
            queryWatch.reset();
            queryWatch.start();
            queryWatch.suspend();

            int queryNumbers = tripleGraph.getObjects().size();

            for (int i = 0; i < iterations; i++) {
                storage.clearDB();
                createWatch.resume();
                model.storeTriples();
                createWatch.suspend();
                queryWatch.resume();
                List<String> objects = model.queryObjects(queryNumbers);
                queryWatch.suspend();
//                System.out.printf("objects: %d%n", objects.size());
//                for (String obj : objects) {
//                    System.out.printf("Object: %s%n", obj);
//                }
            }

            queryWatch.stop();

            System.out.printf("create time: %.2f%n", (float) createWatch.getTime(timeUnit) / iterations);
            System.out.printf("query time: %.2f%n", (float) queryWatch.getTime(timeUnit) / iterations);
        }
    }
}
