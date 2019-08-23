package atomspace.performance.storage.neo4j;

import atomspace.performance.generator.Triple;
import org.neo4j.driver.v1.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.neo4j.driver.v1.Values.parameters;

public class DBNeo4jStorage implements Closeable {

    final Driver driver;

    public DBNeo4jStorage() {
        this("bolt://localhost:7687", "neo4j", "test");
    }

    public DBNeo4jStorage(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void putTriples(Set<Triple> triples) {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Triple triple : triples) {
                    tx.run("MERGE (:Person {name: {subject}})  ",
                            parameters("subject", triple.subject));
                    tx.run("MERGE (:Item {name: {object}})  ",
                            parameters("object", triple.object));
                }
                for (Triple triple : triples) {
                    tx.run("MATCH (a:Person {name: {subject}})," +
                                    " (b:Item {name: {object}}) " +
                                    " CREATE (a)-[r:PREDICATE {name: {predicate}}] ->(b)",
                            parameters("subject", triple.subject,
                                    "object", triple.object,
                                    "predicate", triple.predicate));
                }
                tx.success();
            }
        }
    }

    public List<String> queryTripleObject(int iterations, Triple... triples) {

        List<String> objects = new ArrayList<>();

        int size = triples.length;
        Random rand = new Random(42);

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (int i = 0; i < iterations; i++) {

                    Triple triple = triples[rand.nextInt(size)];
                    StatementResult res = tx.run("MATCH (o:Person)-[p:PREDICATE ]->(s:Item) " +
                                    " WHERE o.name = {subject} and p.name = {predicate}" +
                                    " RETURN s.name",
                            parameters("subject", triple.subject,
                                    "predicate", triple.predicate));

                    while (res.hasNext()) {
                        Record record = res.next();
                        String obj = record.get(0).asString();
                        System.out.printf("query object: %s%n", obj);
                        objects.add(obj);
                    }
                }
                tx.success();
            }
        }

        return objects;
    }


    public void clearDB() {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n) DETACH DELETE n;");
                tx.success();
            }
        }
    }

    @Override
    public void close() throws IOException {
        driver.close();
    }
}
