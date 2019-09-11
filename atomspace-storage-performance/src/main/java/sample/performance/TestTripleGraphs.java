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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static TripleGraph getRandomTripleGraph(int N, int statements) {
        int subjectsNumber = N;
        int predicatesNumber = N / 4;
        int objectsNumber = N;

        return new RandomTripleGraph(
                subjectsNumber,
                predicatesNumber,
                objectsNumber,
                statements);
    }


    public static void runRequests(DBStorage storage,
                                   int queries,
                                   boolean showQueries,
                                   TripleModel... models) {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        int totalObjects = 0;
        int iterations = 4;

        System.out.printf("iterations: %d%n", iterations);

        Map<String, Double> createTimeMap = new HashMap<>();
        Map<String, Double> queryTimeMap = new HashMap<>();

        // warmup
        for (TripleModel model : models) {
            storage.clearDB();
            model.storeTriples();
            List<String> objects = model.queryObjects(queries);
            objects.size();
        }

        for (TripleModel model : models) {

            StopWatch createWatch = createAndInitStopWatch();
            StopWatch queryWatch = createAndInitStopWatch();


            for (int i = 0; i < iterations; i++) {
                storage.clearDB();
                createWatch.resume();
                model.storeTriples();
                createWatch.suspend();
                queryWatch.resume();
                List<String> objects = model.queryObjects(queries);
                totalObjects += objects.size();
                queryWatch.suspend();
                if (showQueries) {
                    System.out.printf("objects: %d%n", objects.size());
                    for (String obj : objects) {
                        System.out.printf("Object: %s%n", obj);
                    }
                }
            }

            queryWatch.stop();

            double createTime = ((double) createWatch.getTime(timeUnit)) / iterations;
            double queryTime = ((double) queryWatch.getTime(timeUnit)) / iterations;
            System.out.printf("model: %s, create: %.2f, query: %.2f%n", model.getName(), createTime, queryTime);

            createTimeMap.put(model.getName(), createTime);
            queryTimeMap.put(model.getName(), queryTime);
        }

        System.out.printf("%n");
        System.out.printf("total query objects: %d%n", totalObjects);
    }

    private static StopWatch createAndInitStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        return stopWatch;
    }
}
