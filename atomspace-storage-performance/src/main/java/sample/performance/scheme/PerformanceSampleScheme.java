package sample.performance.scheme;

import atomspace.performance.storage.TripleAtomModel;
import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.scheme.TripleAtomPredicateSchemeModel;
import atomspace.performance.triple.TripleGraph;
import sample.performance.neo4j.PerformanceTripleGraph;

public class PerformanceSampleScheme {
    public static void main(String[] args) throws Exception {


//        String p = "predicate0129";
//        System.out.println(TripleAtomModel.toLinkLabel(p));
//
//        TripleGraph tripleGraph = new FixedTripleGraph(
//                new Triple("Alice", "likes", "ice-cream"));

//        TripleGraph tripleGraph = new FixedTripleGraph(
//                new Triple("Alice", "likes", "ice-cream"),
//                new Triple("Alice", "likes", "apple"),
//                new Triple("Bob", "likes", "apple")
//        );


        int N = 20;
        TripleGraph tripleGraph = PerformanceTripleGraph.getGraph(N);

        for (String predicate : tripleGraph.getPredicates()) {
            String link = TripleAtomModel.toLinkLabel(predicate);
            String upperCaseLink = link.substring(0, link.length() - 4).toUpperCase();
            System.out.printf("%s_LINK <- ORDERED_LINK%n", upperCaseLink);
        }


        TripleModel model = new TripleAtomPredicateSchemeModel("as", tripleGraph);
//        TripleModel model = new TripleAtomEvaluationSchemeModel("as", tripleGraph);

        int queries = 50;

        model.storeTriples();
        model.queryObjects(queries);
    }
}
