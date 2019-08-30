package atomspace.performance.storage.neo4j;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.triple.TripleGraph;

import java.util.List;

public abstract class TripleNeo4jModel implements TripleModel {

    protected final DBNeo4jStorage storage;
    protected final TripleGraph tripleGraph;

    public TripleNeo4jModel(TripleGraph tripleGraph) {
        this(new DBNeo4jStorage(), tripleGraph);
    }

    public TripleNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        this.storage = storage;
        this.tripleGraph = tripleGraph;
    }
}
