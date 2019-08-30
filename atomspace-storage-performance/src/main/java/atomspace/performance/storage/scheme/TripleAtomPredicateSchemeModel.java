package atomspace.performance.storage.scheme;

import atomspace.performance.DBAtom;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.storage.TripleAtomModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TripleAtomPredicateSchemeModel extends TripleAtomSchemeModel {


    public TripleAtomPredicateSchemeModel(String file, TripleGraph tripleGraph) {
        super(file, tripleGraph);
    }

    @Override
    protected DBAtom toAtom(Triple triple) {
        return TripleAtomModel.toAtomPredicate(atomspace, triple);
    }

    @Override
    public void storeTriples() {

        AtomsSaver saver = new AtomsSaver();

        for (DBAtom atom : atoms) {
            TripleAtomModel.handleAtom(atom, saver);
            saver.nextLine();
        }

        System.out.println(saver.builder);

        saveToFile("create", saver.builder);
    }

    @Override
    public List<String> queryObjects(int iterations) {

        StringBuilder builder = new StringBuilder();

        List<Triple> triples = new LinkedList<>();
        triples.addAll(tripleGraph.getTriples());

        int size = triples.size();
        Random rand = new Random(42);

        for (int i = 0; i < iterations; i++) {
            Triple triple = triples.get(rand.nextInt(size));
            builder.append(String.format("" +
                            "(cog-execute! (Get\n" +
                            "   (InheritanceLink\n" +
                            "           (ConceptNode \"%s\")\n" +
                            "           (VariableNode \"$WHAT\"))\n" +
                            "))\n\n",
                    triple.subject));
        }

        saveToFile("query", builder);

        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return "Evaluation";
    }
}
