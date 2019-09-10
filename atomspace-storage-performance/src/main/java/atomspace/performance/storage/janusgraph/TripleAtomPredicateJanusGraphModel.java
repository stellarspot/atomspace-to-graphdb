package atomspace.performance.storage.janusgraph;

import atomspace.performance.DBAtom;
import atomspace.performance.storage.neo4japi.DBNeo4jAPIStorage;
import atomspace.performance.storage.neo4japi.TripleAtomNeo4jAPIModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleAtomPredicateJanusGraphModel extends TripleAtomJanusGraphModel {

    public TripleAtomPredicateJanusGraphModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomPredicateJanusGraphModel(DBJanusGraphStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Predicate";
    }

    // PredicateLink(SubjectNode, ObjectNode)
    @Override
    protected DBAtom toAtom(Triple triple) {
        return atomspace.getLink(toLinkLabel(triple.predicate),
                atomspace.getNode("SubjectNode", triple.subject),
                atomspace.getNode("ObjectNode", triple.object));

    }

    // PredicateLink(*, ObjectNode)
    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

        GraphTraversalSource g = storage.graph.traversal();

        for (int i = 0; i < iterations; i++) {
            Triple triple = triples.get(rand.nextInt(size));

            GraphTraversal<Vertex, Vertex> iter = g.V()
                    .hasLabel(SUBJECT_LABEL)
                    .has("value", triple.subject)
                    .out(getArgType(0))
                    .hasLabel(toLinkLabel(triple.predicate))
                    .in(getArgType(1))
                    .hasLabel(OBJECT_LABEL);

            while (iter.hasNext()) {
                objects.add(iter.next().value("value"));
            }
        }

        return objects;
    }

    private static String toLinkLabel(String name) {
        return String.format("%s_LINK", name.toUpperCase());
    }
}
