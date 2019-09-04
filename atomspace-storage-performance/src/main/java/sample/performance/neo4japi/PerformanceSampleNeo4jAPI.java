package sample.performance.neo4japi;

import atomspace.performance.storage.neo4j.*;
import atomspace.performance.storage.neo4japi.*;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.TripleGraph;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceSampleNeo4jAPI {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jAPIStorage storage = new DBNeo4jAPIStorage()) {


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

            TripleNeo4jAPIModel nativeModel = new TripleNativeNeo4jAPIModel(storage, tripleGraph);
            TripleNeo4jAPIModel predicateModel = new TripleAtomPredicateNeo4jAPIModel(storage, tripleGraph);
            TripleNeo4jAPIModel evaluationModel = new TripleAtomEvaluationNeo4jAPIModel(storage, tripleGraph);

            measureRequests(storage, tripleGraph, 4, nativeModel);
//            measureRequests(storage, tripleGraph, 4, nativeModel, predicateModel, evaluationModel);
//            measureRequests(storage, tripleGraph, 4, evaluationModel, nativeModel, predicateModel, evaluationModel);
        }
    }

    private static void measureRequests(DBNeo4jAPIStorage storage,
                                        TripleGraph tripleGraph,
                                        int iterations,
                                        TripleNeo4jAPIModel... models) {
        System.out.printf("triples: %s%n", tripleGraph.getStatistics());
        System.out.printf("iterations: %d%n", iterations);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        for (TripleNeo4jAPIModel model : models) {

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
                System.out.printf("objects: %d%n", objects.size());
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
