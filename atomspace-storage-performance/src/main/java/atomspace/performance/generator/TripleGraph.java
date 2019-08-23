package atomspace.performance.generator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TripleGraph {

    public final int subjectsNumber;
    public final int predicatesNumber;
    public final int objectsNumber;
    public final int predicatesPerSubjectNumber;

    private final Set<String> subjects = new HashSet<>();
    private final Set<String> objects = new HashSet<>();
    private final Set<String> predicates = new HashSet<>();
    private final Set<Triple> triples = new HashSet<>();

    public TripleGraph() {
        this(10, 3, 10, 3);
    }

    public TripleGraph(int subjectsNumber,
                       int predicatesNumber,
                       int objectsNumber,
                       int predicatesPerSubjectNumber) {
        this.subjectsNumber = subjectsNumber;
        this.predicatesNumber = predicatesNumber;
        this.objectsNumber = objectsNumber;
        this.predicatesPerSubjectNumber = predicatesPerSubjectNumber;
        initGenerator();
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public Set<String> getObjects() {
        return objects;
    }

    public Set<String> getPredicates() {
        return predicates;
    }

    public Set<Triple> getTriples() {
        return triples;
    }

    /**
     * Generate triples:
     * (subject, predicate, object)
     */
    private void initGenerator() {
        Random rand = new Random(42);
        for (int i = 0; i < subjectsNumber; i++) {
            for (int j = 0; j < predicatesPerSubjectNumber; j++) {
                while (true) {
                    Triple triple = generateTriple(rand);
                    if (!triples.contains(triple)) {
                        addTriple(triple);
                        break;
                    }
                }
            }
        }
    }

    private Triple generateTriple(Random rand) {
        return new Triple(
                rand.nextInt(subjectsNumber),
                rand.nextInt(predicatesNumber),
                rand.nextInt(objectsNumber));
    }

    private void addTriple(Triple triple) {
        subjects.add(triple.subject);
        predicates.add(triple.predicate);
        objects.add(triple.object);
        triples.add(triple);
    }
}
