package sample.performance;

import atomspace.performance.storage.DBStorage;
import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.storage.neo4japi.TripleNeo4jAPIModel;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestTripleGraphs {


    public static TripleGraph getTripleGraph1() {
        return new FixedTripleGraph(
                new Triple("Alice", "likes", "ice-cream"));
    }

    public static TripleGraph getTripleGraph3() {
        return new FixedTripleGraph(
                new Triple("Alice", "likes", "ice-cream"),
                new Triple("Alice", "likes", "apple"),
                new Triple("Bob", "likes", "apple"));
    }

    public static TripleGraph getRandomTripleGraph(int N) {
        int subjectsNumber = N;
        int predicatesNumber = N / 2;
        int objectsNumber = N;
        int predicatesPerSubjectNumber = N / 4;

        return new RandomTripleGraph(
                subjectsNumber,
                predicatesNumber,
                objectsNumber,
                predicatesPerSubjectNumber);
    }


    public static void runRequests(DBStorage storage,
                                   int iterations,
                                   boolean showQueries,
                                   TripleModel... models) {
        System.out.printf("iterations: %d%n", iterations);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        for (TripleModel model : models) {

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
                if (showQueries) {
                    System.out.printf("objects: %d%n", objects.size());
                    for (String obj : objects) {
                        System.out.printf("Object: %s%n", obj);
                    }
                }
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
