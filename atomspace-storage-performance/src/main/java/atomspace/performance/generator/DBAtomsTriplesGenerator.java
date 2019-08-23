package atomspace.performance.generator;

import atomspace.performance.DBAtom;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;

import java.util.*;

public class DBAtomsTriplesGenerator implements DBAtomsGenerator {


    public final TripleGraph tripleGraph;

    private final List<DBAtom> atoms = new ArrayList<>();
    private final List<Triple> triples = new ArrayList<>();

    private long id = 0;

    private static final String TYPE_EVALUATION = "EvaluationLink";
    private static final String TYPE_LIST = "ListLink";
    private static final String TYPE_PREDICATE = "PredicateNode";
    private static final String TYPE_CONCEPT = "ConceptNode";

    public DBAtomsTriplesGenerator() {
        this(new TripleGraph());
    }

    public DBAtomsTriplesGenerator(TripleGraph tripleGraph) {
        this.tripleGraph = tripleGraph;
        initGenerator();
    }

    @Override
    public List<DBAtom> getAtoms() {
        return atoms;
    }

    public TripleGraph getTripleGraph() {
        return tripleGraph;
    }

    private long nextId() {
        return id++;
    }

    private DBAtom toAtom(Triple triple) {
        return new DBLink(nextId(), TYPE_EVALUATION,
                new DBNode(nextId(), TYPE_PREDICATE, triple.predicate),
                new DBLink(nextId(), TYPE_LIST,
                        new DBNode(nextId(), TYPE_CONCEPT, triple.subject),
                        new DBNode(nextId(), TYPE_CONCEPT, triple.object)));
    }

    /**
     * Generate triples:
     * (subject, predicate, object)
     * <p>
     * Evalutation
     * ..Predicate "predicate"
     * ..List
     * ....Concept "subject"
     * ....Concept "object"
     */
    private void initGenerator() {

        for (Triple triple : tripleGraph.getTriples()) {
            atoms.add(toAtom(triple));
        }
    }
}
