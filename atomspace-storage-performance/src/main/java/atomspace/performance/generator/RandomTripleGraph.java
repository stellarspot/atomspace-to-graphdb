package atomspace.performance.generator;

import java.util.Random;

public class RandomTripleGraph extends TripleGraph {

    public final int subjectsNumber;
    public final int predicatesNumber;
    public final int objectsNumber;
    public final int predicatesPerSubjectNumber;


    public RandomTripleGraph() {
        this(10, 3, 10, 3);
    }

    public RandomTripleGraph(int subjectsNumber,
                             int predicatesNumber,
                             int objectsNumber,
                             int predicatesPerSubjectNumber) {
        this.subjectsNumber = subjectsNumber;
        this.predicatesNumber = predicatesNumber;
        this.objectsNumber = objectsNumber;
        this.predicatesPerSubjectNumber = predicatesPerSubjectNumber;
        initGraph();
    }

    /**
     * Generate triples:
     * (subject, predicate, object)
     */
    private void initGraph() {
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
