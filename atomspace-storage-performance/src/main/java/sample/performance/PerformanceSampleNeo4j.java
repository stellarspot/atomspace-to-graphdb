package sample.performance;

import atomspace.performance.generator.FixedTripleGraph;
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

            TripleGraph tripleGraph = new FixedTripleGraph(
                    new Triple("Alice", "likes", "ice-cream"),
                    new Triple("Alice", "likes", "apple"),
                    new Triple("Bob", "likes", "apple")
            );

//            TripleNeo4jModel model = new TripleNativeNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel model = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
            TripleNeo4jModel model = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);
            model.storeTriples();

            List<String> objects = model.queryObject();
            for (String obj : objects) {
                System.out.printf("Object: %s%n", obj);
            }
        }
    }
}
