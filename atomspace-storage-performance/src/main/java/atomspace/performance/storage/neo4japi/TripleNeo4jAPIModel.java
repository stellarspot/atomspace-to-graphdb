package atomspace.performance.storage.neo4japi;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4j.DBNeo4jStorage;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.Label;

public abstract class TripleNeo4jAPIModel implements TripleModel {

    protected static final Label SUBJECT_LABEL = Label.label("SubjectNode");
    protected static final Label OBJECT_LABEL = Label.label("ObjectNode");

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
