package atomspace.performance.storage.janusgraph;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.triple.TripleGraph;
import org.janusgraph.core.JanusGraph;
import org.neo4j.graphdb.Label;

public abstract class TripleJanusGraphModel implements TripleModel {

    static final String SUBJECT_NODE = "SubjectNode";
    static final String OBJECT_NODE = "ObjectNode";
    static final String PREDICATE_NODE = "PredicateNode";
    static final String EVALUATION_LINK = "EvaluationLink";
    static final String LIST_LINK = "ListList";


    protected final DBJanusGraphStorage storage;
    protected final TripleGraph tripleGraph;

    public TripleJanusGraphModel(TripleGraph tripleGraph) {
        this(new DBJanusGraphStorage(), tripleGraph);
    }

    public TripleJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        this.storage = storage;
        this.tripleGraph = tripleGraph;
    }
}
