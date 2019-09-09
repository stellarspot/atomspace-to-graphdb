package sample.performance.neo4j;

import atomspace.performance.storage.neo4j.*;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.commons.lang3.time.StopWatch;
import sample.performance.TestTripleGraphs;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SampleNeo4j {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {


//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"));

//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"),
//                    new Triple("Alice", "likes", "apple"),
//                    new Triple("Bob", "likes", "apple")
//            );


            TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(8, 12);


            TripleNeo4jModel model = new TripleNativeNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel model = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel model = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);


            System.out.printf("model: %s", tripleGraph.getStatistics());
            storage.clearDB();
            model.storeTriples();

            List<String> objects = model.queryObject();
            for (String obj : objects) {
                System.out.printf("Object: %s%n", obj);
            }

        }
    }
}
