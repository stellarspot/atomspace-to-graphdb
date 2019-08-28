package atomspace.performance.storage.neo4j;

import atomspace.performance.DBAtom;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.neo4j.driver.v1.Values.parameters;

public class TripleAtomEvaluationNeo4jModel extends TripleAtomNeo4jModel {

    private static final String TYPE_EVALUATION = "EvaluationLink";
    private static final String TYPE_LIST = "ListLink";
    private static final String TYPE_PREDICATE = "PredicateNode";
    private static final String TYPE_CONCEPT = "ConceptNode";

    public TripleAtomEvaluationNeo4jModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomEvaluationNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Evaluation";
    }

    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(TYPE_EVALUATION,
                atomspace.getNode(TYPE_PREDICATE, triple.predicate),
                atomspace.getLink(TYPE_LIST,
                        atomspace.getNode(TYPE_CONCEPT, triple.subject),
                        atomspace.getNode(TYPE_CONCEPT, triple.object)));
    }

    // EvaluationLink
    //  Predicate "predicate"
    //      ListLink
    //          Concept "subject"
    //          *
    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

        try (Session session = storage.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (int i = 0; i < iterations; i++) {

                    Triple triple = triples.get(rand.nextInt(size));

                    // (Evaluation)-[{position: 0}]->(Predicate)
                    // (Evaluation)-[{position: 1}]->(ListLink)
                    // (ListLink)-[{position: 0}]->(Subject)
                    // (ListLink)-[{position: 1}]->(Object)

                    StatementResult res = tx.run("MATCH" +
                                    " (e:Atom:Link {type: 'EvaluationLink'}) - [:ARG {position: 0}] -> (:Atom:Node {value: {predicate}})," +
                                    " (e) - [:ARG {position: 1}] -> (l:Atom:Link {type: 'ListLink'})," +
                                    " (l) - [:ARG {position: 0}] -> (:Atom:Node {type: 'ConceptNode', value: {subject}}), " +
                                    " (l) - [:ARG {position: 1}] -> (o:Atom:Node {type: 'ConceptNode'})" +
                                    " RETURN o.value",
                            parameters("subject", triple.subject,
                                    "predicate", triple.predicate));

                    while (res.hasNext()) {
                        Record record = res.next();
                        String obj = record.get(0).asString();
                        objects.add(obj);
                    }
                }
                tx.success();
            }
        }

        return objects;
    }
}
