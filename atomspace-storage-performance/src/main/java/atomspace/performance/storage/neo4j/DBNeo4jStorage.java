package atomspace.performance.storage.neo4j;

import atomspace.performance.storage.DBStorage;
import org.neo4j.driver.v1.*;

public class DBNeo4jStorage implements DBStorage {

    final Driver driver;

    public DBNeo4jStorage() {
        this("bolt://localhost:7687", "neo4j", "test");
    }

    public DBNeo4jStorage(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("CREATE INDEX ON :Atom(id)");
                tx.run("CREATE INDEX ON :Atom(type)");
                tx.run("CREATE INDEX ON :ARG(position)");
                tx.run("CREATE INDEX ON :Subject(value)");
                tx.run("CREATE INDEX ON :Object(value)");
                tx.success();
            }
        }
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
    public void close() {
        driver.close();
    }
}
