package sample.performance.neo4j;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4j.DBNeo4jStorage;
import atomspace.performance.storage.neo4j.TripleAtomEvaluationNeo4jModel;
import atomspace.performance.storage.neo4j.TripleAtomPredicateNeo4jModel;
import atomspace.performance.storage.neo4j.TripleNativeNeo4jModel;
import atomspace.performance.triple.TripleGraph;
import sample.performance.TestTripleGraphs;

public class PerformanceSampleNeo4j {
    public static void main(String[] args) throws Exception {

        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {

            int N = 500;
            int statements = 500;
            int queries = 100;
//            int N = 50;
//            int statements = 40;
//            int queries = 2;

//            TripleGraph tripleGraph = TestTripleGraphs.getTripleGraph3();
            TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(N, statements);

            TripleModel nativeModel = new TripleNativeNeo4jModel(storage, tripleGraph);
            TripleModel predicateModel = new TripleAtomPredicateNeo4jModel(storage, tripleGraph);
            TripleModel evaluationModel = new TripleAtomEvaluationNeo4jModel(storage, tripleGraph);

            System.out.printf("triples: %s%n", tripleGraph.getStatistics());
            TestTripleGraphs.runRequests(storage, queries, false, nativeModel, predicateModel, evaluationModel);
        }
    }
}