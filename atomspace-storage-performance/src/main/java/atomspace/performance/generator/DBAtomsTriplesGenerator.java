package atomspace.performance.generator;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;

import java.util.ArrayList;
import java.util.List;

public class DBAtomsTriplesGenerator implements DBAtomsGenerator {


    public final TripleGraph tripleGraph;

    private final DBAtomSpace atomspace = new DBAtomSpace();
    private final List<DBAtom> atoms = new ArrayList<>();
    private final List<Triple> triples = new ArrayList<>();

    private static final String TYPE_EVALUATION = "EvaluationLink";
    private static final String TYPE_LIST = "ListLink";
    private static final String TYPE_PREDICATE = "PredicateNode";
    private static final String TYPE_CONCEPT = "ConceptNode";

    public DBAtomsTriplesGenerator() {
        this(new RandomTripleGraph());
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

    private DBAtom toAtom(Triple triple) {
        return atomspace.getLink(TYPE_EVALUATION,
                atomspace.getNode(TYPE_PREDICATE, triple.predicate),
                atomspace.getLink(TYPE_LIST,
                        atomspace.getNode(TYPE_CONCEPT, triple.subject),
                        atomspace.getNode(TYPE_CONCEPT, triple.object)));
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
