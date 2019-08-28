package atomspace.performance.storage.neo4j;

import atomspace.performance.generator.TripleGraph;

import java.util.List;

public abstract class TripleNeo4jModel {

    protected final DBNeo4jStorage storage;
    protected final TripleGraph tripleGraph;

    public TripleNeo4jModel(TripleGraph tripleGraph) {
        this(new DBNeo4jStorage(), tripleGraph);
    }

    public TripleNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        this.storage = storage;
        this.tripleGraph = tripleGraph;
    }

    // Alice likes ice-cream.
    // (Alice, likes, ice-cream)
    public abstract void storeTriples();

    // What does Alice like?
    public List<String> queryObject() {
        return queryObjects(1);
    }

    public abstract List<String> queryObjects(int iterations);
}
