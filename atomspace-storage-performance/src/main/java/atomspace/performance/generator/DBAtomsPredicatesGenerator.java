package atomspace.performance.generator;

import atomspace.performance.DBAtom;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;

import java.util.*;

public class DBAtomsPredicatesGenerator implements DBAtomsGenerator {

    public final int subjectsNumber;
    public final int predicatesNumber;
    public final int objectsNumber;
    public final int predicatesPerSubjectNumber;

    private final List<DBAtom> atoms = new ArrayList<>();
    private final List<Triple> triples = new ArrayList<>();

    private long id = 0;

    private static final String TYPE_EVALUATION = "EvaluationLink";
    private static final String TYPE_LIST = "ListLink";
    private static final String TYPE_PREDICATE = "PredicateNode";
    private static final String TYPE_CONCEPT = "ConceptNode";

    public DBAtomsPredicatesGenerator() {
        this(10, 3, 10, 3);
    }

    public DBAtomsPredicatesGenerator(int subjectsNumber,
                                      int predicatesNumber,
                                      int objectsNumber,
                                      int predicatesPerSubjectNumber) {
        this.subjectsNumber = subjectsNumber;
        this.predicatesNumber = predicatesNumber;
        this.objectsNumber = objectsNumber;
        this.predicatesPerSubjectNumber = predicatesPerSubjectNumber;
        initGenerator();
    }

    @Override
    public List<DBAtom> getAtoms() {
        return atoms;
    }

    public List<Triple> getTriples() {
        return triples;
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
        Random rand = new Random(42);
        Set<Triple> set = new HashSet<>();
        for (int i = 0; i < subjectsNumber; i++) {
            for (int j = 0; j < predicatesPerSubjectNumber; j++) {
                while (true) {
                    Triple triple = generateTriple(rand);
                    if (!set.contains(triple)) {
                        set.add(triple);
                        break;
                    }
                }
            }
        }

        triples.addAll(set);
        for (Triple triple : triples) {
            atoms.add(toAtom(triple));
        }
    }

    private Triple generateTriple(Random rand) {
        return new Triple(
                rand.nextInt(subjectsNumber),
                rand.nextInt(predicatesNumber),
                rand.nextInt(objectsNumber));
    }
}
