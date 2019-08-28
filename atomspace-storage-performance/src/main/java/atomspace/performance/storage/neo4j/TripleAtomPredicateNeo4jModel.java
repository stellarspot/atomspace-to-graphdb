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

public class TripleAtomPredicateNeo4jModel extends TripleAtomNeo4jModel {

    public TripleAtomPredicateNeo4jModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomPredicateNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Predicate";
    }

    // PredicateLink(SubjectNode, ObjectNode)
    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(toLinkLabel(triple.predicate),
                atomspace.getNode("Subject", triple.subject),
                atomspace.getNode("Object", triple.object));

    }

    // PredicateLink(*, ObjectNode)
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

                    // (Predicate)-[{position: 0}]->(Subject)
                    // (Object)-[{position: 1}]->(Object)

                    StatementResult res = tx.run("MATCH" +
                                    " (p:Atom:Link {type: {predicate_type}})-[{position: 0}]-> (:Atom:Node {type: {subject_type}, value: {subject}})," +
                                    " (p)-[{position: 1}]-> (o:Atom:Node {type: {object_type}})" +
                                    " RETURN o.value",
                            parameters("subject", triple.subject,
                                    "subject_type", "Subject",
                                    "predicate_type", toLinkLabel(triple.predicate),
                                    "object_type", "Object"));

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

    private static String toLinkLabel(String name) {
        return String.format("%s_LINK", name.toUpperCase());
    }
}
