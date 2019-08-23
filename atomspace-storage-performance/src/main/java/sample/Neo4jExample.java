package sample;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Neo4jExample {
    // Driver objects are thread-safe and are typically made available application-wide.
    Driver driver;

    public Neo4jExample(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    private void addPerson(String name) {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session()) {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MERGE (a:Person {name: {x}})", parameters("x", name));
                tx.success();  // Mark this write as successful.
            }
        }
    }

    private void printPeople(String initial) {
        try (Session session = driver.session()) {
            // Auto-commit transactions are a quick and easy way to wrap a read.
            StatementResult result = session.run(
                    "MATCH (a:Person) WHERE a.name STARTS WITH {x} RETURN a.name AS name",
                    parameters("x", initial));
            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                // Values can be extracted from a record by index or name.
                System.out.println(record.get("name").asString());
            }
        }
    }

    public void close() {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }

    public static void main(String... args) {
        Neo4jExample example = new Neo4jExample("bolt://localhost:7687", "neo4j", "test");
        example.addPerson("Ada");
        example.addPerson("Alice");
        example.addPerson("Bob");
        example.printPeople("A");
        example.close();
    }
}