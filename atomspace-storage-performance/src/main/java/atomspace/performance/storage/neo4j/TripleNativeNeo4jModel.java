package atomspace.performance.storage.neo4j;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;

public class TripleNativeNeo4jModel extends TripleNeo4jModel {

    public TripleNativeNeo4jModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleNativeNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Native";
    }

    @Override
    public void storeTriples() {
        try (Session session = storage.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Triple triple : tripleGraph.getTriples()) {
                    tx.run("MERGE (:Subject {value: {subject}})  ",
                            parameters("subject", triple.subject));
                    tx.run("MERGE (:Object {value: {object}})  ",
                            parameters("object", triple.object));
                }
                for (Triple triple : tripleGraph.getTriples()) {
                    tx.run("MATCH (a:Subject {value: {subject}})," +
                                    " (b:Object {value: {object}}) " +
                                    " CREATE (a)-[r:PREDICATE {value: {predicate}}] ->(b)",
                            parameters("subject", triple.subject,
                                    "object", triple.object,
                                    "predicate", triple.predicate));
                }
                tx.success();
            }
        }
    }

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
                    StatementResult res = tx.run("MATCH (o:Subject)-[p:PREDICATE ]->(s:Object) " +
                                    " WHERE o.value = {subject} and p.value = {predicate}" +
                                    " RETURN s.value",
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
