package atomspace.performance.storage.neo4j;

import org.neo4j.driver.v1.*;

import java.io.Closeable;
import java.io.IOException;

public class DBNeo4jStorage implements Closeable {

    final Driver driver;

    public DBNeo4jStorage() {
        this("bolt://localhost:7687", "neo4j", "test");
    }

    public DBNeo4jStorage(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
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
