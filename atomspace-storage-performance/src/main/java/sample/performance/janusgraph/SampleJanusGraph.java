package sample.performance.janusgraph;

import atomspace.performance.storage.janusgraph.DBJanusGraphStorage;
import atomspace.performance.storage.janusgraph.TripleAtomPredicateJanusGraphModel;
import atomspace.performance.storage.janusgraph.TripleNativeJanusGraphModel;
import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.storage.neo4japi.TripleNativeNeo4jAPIModel;
import atomspace.performance.storage.neo4japi.TripleNeo4jAPIModel;
import atomspace.performance.triple.FixedTripleGraph;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import sample.performance.TestTripleGraphs;

import java.util.List;

public class SampleJanusGraph {
    public static void main(String[] args) throws Exception {
        try (DBJanusGraphStorage storage = new DBJanusGraphStorage()) {


//            TripleGraph tripleGraph = new FixedTripleGraph(
//                    new Triple("Alice", "likes", "ice-cream"));

            TripleGraph tripleGraph = new FixedTripleGraph(
                    new Triple("Alice", "likes", "ice-cream"),
//                    new Triple("Alice", "likes", "apple"),
//                    new Triple("Alice", "dislikes", "pear"),
//                    new Triple("Bob", "likes", "apple")
                    new Triple("Alice", "dislikes", "pear")

            );


//            int N = 8;
//            int statements = 4;
//            TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(N, statements);

//            TripleNativeJanusGraphModel model = new TripleNativeJanusGraphModel(storage, tripleGraph);
            TripleAtomPredicateJanusGraphModel model = new TripleAtomPredicateJanusGraphModel(storage, tripleGraph);
//            TripleNeo4jAPIModel model = new TripleAtomEvaluationNeo4jAPIModel(storage, tripleGraph);


            System.out.printf("model: %s", tripleGraph.getStatistics());
            storage.clearDB();
            model.storeTriples();

            storage.dump();

            List<String> objects = model.queryObjects(4);
//            System.out.printf("objects: %d%n", objects.size());
            for (String obj : objects) {
                System.out.printf("Object: %s%n", obj);
            }
        }

        System.exit(0);
    }
}
