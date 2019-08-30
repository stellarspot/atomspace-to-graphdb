package sample.performance.scheme;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4j.*;
import atomspace.performance.storage.scheme.TripleAtomEvaluationSchemeModel;
import atomspace.performance.storage.scheme.TripleAtomSchemeModel;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.commons.lang3.time.StopWatch;
import sample.performance.PerformanceTripleGraph;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceSampleScheme {
    public static void main(String[] args) throws Exception {


//        TripleGraph tripleGraph = new FixedTripleGraph(
//                new Triple("Alice", "likes", "ice-cream"));

//        TripleGraph tripleGraph = new FixedTripleGraph(
//                new Triple("Alice", "likes", "ice-cream"),
//                new Triple("Alice", "likes", "apple"),
//                new Triple("Bob", "likes", "apple")
//        );


        int N = 80;
        TripleGraph tripleGraph = PerformanceTripleGraph.getGraph(N);

        TripleModel model = new TripleAtomEvaluationSchemeModel("as", tripleGraph);

        int queries = 50;

        model.storeTriples();
        model.queryObjects(queries);
    }
}
