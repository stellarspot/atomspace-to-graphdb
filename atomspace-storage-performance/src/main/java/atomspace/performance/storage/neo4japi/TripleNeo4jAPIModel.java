package atomspace.performance.storage.neo4japi;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4j.DBNeo4jStorage;
import atomspace.performance.triple.TripleGraph;

public abstract class TripleNeo4jAPIModel implements TripleModel {

    protected final DBNeo4jAPIStorage storage;
    protected final TripleGraph tripleGraph;

    public TripleNeo4jAPIModel(TripleGraph tripleGraph) {
        this(new DBNeo4jAPIStorage(), tripleGraph);
    }

    public TripleNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        this.storage = storage;
        this.tripleGraph = tripleGraph;
    }
}
