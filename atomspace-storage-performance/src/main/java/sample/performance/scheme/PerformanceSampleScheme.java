package sample.performance.scheme;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.scheme.TripleAtomPredicateSchemeModel;
import atomspace.performance.triple.TripleGraph;
import sample.performance.TestTripleGraphs;

public class PerformanceSampleScheme {
    public static void main(String[] args) throws Exception {

        int N = 100;
        int statements = 20;
        int queries = 20;

        TripleGraph tripleGraph = TestTripleGraphs.getRandomTripleGraph(N, statements);

//        for (String predicate : tripleGraph.getPredicates()) {
//            String link = TripleAtomModel.toLinkLabel(predicate);
//            String upperCaseLink = link.substring(0, link.length() - 4).toUpperCase();
//            System.out.printf("%s_LINK <- ORDERED_LINK%n", upperCaseLink);
//        }


        TripleModel model = new TripleAtomPredicateSchemeModel("as", tripleGraph);
//        TripleModel model = new TripleAtomEvaluationSchemeModel("as", tripleGraph);

        model.storeTriples();
        model.queryObjects(queries);
    }
}
