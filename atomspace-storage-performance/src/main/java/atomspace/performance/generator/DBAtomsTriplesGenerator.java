package atomspace.performance.generator;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBAtomsTriplesGenerator implements DBAtomsGenerator {


    public final TripleGraph tripleGraph;

    private final DBAtomSpace atomspace = new DBAtomSpace();
    private final List<DBAtom> atoms = new ArrayList<>();
    private final List<Triple> triples = new ArrayList<>();
    private final TrippleMapper mapper;

    private static final String TYPE_EVALUATION = "EvaluationLink";
    private static final String TYPE_LIST = "ListLink";
    private static final String TYPE_PREDICATE = "PredicateNode";
    private static final String TYPE_CONCEPT = "ConceptNode";

    interface TrippleMapper {
        DBAtom toAtom(DBAtomSpace atomspace, Triple triple);
    }

    public static final TrippleMapper PREDICATE_MAPPER = (atomspace, triple) ->
            atomspace.getLink(String.format("%s_LINK", triple.predicate.toUpperCase()),
                    atomspace.getNode("Subject", triple.subject),
                    atomspace.getNode("Object", triple.object));

    public static final TrippleMapper EVALUATION_PREDICATE_MAPPER = (atomspace, triple) ->
            atomspace.getLink(TYPE_EVALUATION,
                    atomspace.getNode(TYPE_PREDICATE, triple.predicate),
                    atomspace.getLink(TYPE_LIST,
                            atomspace.getNode(TYPE_CONCEPT, triple.subject),
                            atomspace.getNode(TYPE_CONCEPT, triple.object)));

    public DBAtomsTriplesGenerator() {
        this(new RandomTripleGraph(), PREDICATE_MAPPER);
    }

    public DBAtomsTriplesGenerator(TripleGraph tripleGraph, TrippleMapper mapper) {
        this.tripleGraph = tripleGraph;
        this.mapper = mapper;
        initGenerator();
    }

    @Override
    public List<DBAtom> getAtoms() {
        return atoms;
    }

    public TripleGraph getTripleGraph() {
        return tripleGraph;
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
            atoms.add(mapper.toAtom(atomspace, triple));
        }
    }
}
