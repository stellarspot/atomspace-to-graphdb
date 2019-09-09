package atomspace.performance.triple;

import java.util.Random;
import java.util.Set;

public class RandomTripleGraph extends TripleGraph {

    public final int subjectsNumber;
    public final int predicatesNumber;
    public final int objectsNumber;
    public final int statements;


    public RandomTripleGraph() {
        this(10, 3, 10, 3);
    }

    public RandomTripleGraph(int subjectsNumber,
                             int predicatesNumber,
                             int objectsNumber,
                             int statements) {
        this.subjectsNumber = subjectsNumber;
        this.predicatesNumber = predicatesNumber;
        this.objectsNumber = objectsNumber;
        this.statements = statements;
        initGraph();
    }

    /**
     * Generate triples:
     * (subject, predicate, object)
     */
    private void initGraph() {
        Random rand = new Random(42);

        initSet(subjects, "subject", subjectsNumber);
        initSet(objects, "object", objectsNumber);
        initSet(predicates, "predicate", predicatesNumber);

        for (int i = 0; i < statements; i++) {
            while (true) {
                Triple triple = generateTriple(rand);
                if (!triples.contains(triple)) {
                    addTriple(triple);
                    break;
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

    private void initSet(Set<String> set, String prefix, int number) {
        for (int i = 0; i < number; i++) {
            set.add(String.format("%s-%d", prefix, i));
        }
    }
}
