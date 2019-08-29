package sample.performance;

import atomspace.performance.storage.neo4j.*;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SampleNeo4j {
    public static void main(String[] args) throws Exception {
        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {


            TripleGraph tripleGraph = new FixedTripleGraph(
                    new Triple("Alice", "likes", "ice-cream"));

//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"),
//                    new Triple("Alice", "likes", "apple"),
//                    new Triple("Bob", "likes", "apple")
//            );


            TripleNeo4jModel nativeModel = new TripleNativeNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel predicateModel = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
//            TripleNeo4jModel evaluationModel = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);


            storage.clearDB();
            nativeModel.storeTriples();
        }
    }
}
