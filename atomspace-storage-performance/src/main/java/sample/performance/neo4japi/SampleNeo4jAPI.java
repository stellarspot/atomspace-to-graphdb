package sample.performance.neo4japi;

import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.storage.neo4japi.TripleNativeNeo4jAPIModel;
import atomspace.performance.storage.neo4japi.TripleNeo4jAPIModel;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SampleNeo4jAPI {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jAPIStorage storage = new DBNeo4jAPIStorage()) {


//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"));

            TripleGraph tripleGraph = new FixedTripleGraph(
                    new Triple("Alice", "likes", "ice-cream"),
                    new Triple("Alice", "likes", "apple"),
                    new Triple("Bob", "likes", "apple")
            );


//            int N = 8;
//            RandomTripleGraph tripleGraph = new RandomTripleGraph(N, N / 2, N, N / 4);

            List<String> p = new ArrayList<>();
            p.addAll(tripleGraph.getPredicates());
            Collections.sort(p);

            for (String predicate : p) {
                String s = predicate.replace('-', '_');
                System.out.printf("%s,%n", s.toUpperCase());
            }

            TripleNeo4jAPIModel model = new TripleNativeNeo4jAPIModel(storage, tripleGraph);
//            TripleNeo4jModel model = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel model = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);


            System.out.printf("model: %s", tripleGraph.getStatistics());
            storage.clearDB();
            model.storeTriples();

            storage.dump();

            List<String> objects = model.queryObject();
            for (String obj : objects) {
                System.out.printf("Object: %s%n", obj);
            }
        }
    }
}
