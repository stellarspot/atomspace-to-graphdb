package sample.performance.neo4japi;

import atomspace.performance.storage.neo4japi.*;
import atomspace.performance.triple.TripleGraph;
import sample.performance.TestTripleGraphs;

public class PerformanceSampleNeo4jAPI {
    public static void main(String[] args) throws Exception {

        try (DBNeo4jAPIStorage storage = new DBNeo4jAPIStorage()) {

            int N = 100;
            int statements = 20;
            int queries = 20;

//            TripleGraph tripleGraph = TestTripleGraphs.getTripleGraph3();
            TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(N, statements);

            TripleNeo4jAPIModel nativeModel = new TripleNativeNeo4jAPIModel(storage, tripleGraph);
            TripleNeo4jAPIModel predicateModel = new TripleAtomPredicateNeo4jAPIModel(storage, tripleGraph);
            TripleNeo4jAPIModel evaluationModel = new TripleAtomEvaluationNeo4jAPIModel(storage, tripleGraph);

            System.out.printf("triples: %s%n", tripleGraph.getStatistics());
            TestTripleGraphs.runRequests(storage, queries, false, nativeModel, predicateModel, evaluationModel);
        }
    }
}
