package sample.performance.janusgraph;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.janusgraph.DBJanusGraphStorage;
import atomspace.performance.storage.janusgraph.TripleAtomEvaluationJanusGraphModel;
import atomspace.performance.storage.janusgraph.TripleAtomPredicateJanusGraphModel;
import atomspace.performance.storage.janusgraph.TripleNativeJanusGraphModel;
import atomspace.performance.storage.neo4japi.*;
import atomspace.performance.triple.TripleGraph;
import sample.performance.TestTripleGraphs;

public class PerformanceSampleJanusGraph {
    public static void main(String[] args) throws Exception {

        try (DBJanusGraphStorage storage = new DBJanusGraphStorage()) {

            int N = 500;
            int statements = 300;
            int queries = 2;

//            TripleGraph tripleGraph = TestTripleGraphs.getTripleGraph3();
            TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(N, statements);

            TripleModel nativeModel = new TripleNativeJanusGraphModel(storage, tripleGraph);
            TripleModel predicateModel = new TripleAtomPredicateJanusGraphModel(storage, tripleGraph);
            TripleModel evaluationModel = new TripleAtomEvaluationJanusGraphModel(storage, tripleGraph);

            System.out.printf("triples: %s%n", tripleGraph.getStatistics());
            TestTripleGraphs.runRequests(storage, queries, false, nativeModel, predicateModel, evaluationModel);
        }
    }
}
