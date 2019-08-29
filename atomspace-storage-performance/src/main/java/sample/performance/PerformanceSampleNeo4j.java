package sample.performance;

import atomspace.performance.storage.neo4j.*;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.TripleGraph;
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

            int N = 20;
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

            measureRequests(storage, tripleGraph, 4, nativeModel, predicateModel, evaluationModel);
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

            StopWatch createWatch = createAndInitStopWatch();
            StopWatch queryWatch = createAndInitStopWatch();

            int queryNumbers = 50;

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

            float createTime = (float) createWatch.getTime(timeUnit) / iterations;
            float queryTime = (float) queryWatch.getTime(timeUnit) / iterations;
            System.out.printf("model: %s, create: %.2f, query: %.2f%n", model.getName(), createTime, queryTime);
        }
    }

    private static StopWatch createAndInitStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        return stopWatch;
    }
}
