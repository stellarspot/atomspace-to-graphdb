package atomspace.performance.storage.scheme;

import atomspace.performance.DBAtom;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.storage.TripleAtomModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;

import java.util.LinkedList;
import java.util.List;

public class TripleAtomEvaluationSchemeModel extends TripleAtomSchemeModel {


    public TripleAtomEvaluationSchemeModel(String file, TripleGraph tripleGraph) {
        super(file, tripleGraph);
    }

    @Override
    public void storeTriples() {
        System.out.printf("Store Scheme triples%n");

        AtomsSaver saver = new AtomsSaver();

        for (Triple triple : tripleGraph.getTriples()) {
            DBAtom atom = TripleAtomModel.toAtom(atomspace, triple);
            TripleAtomModel.handleAtom(atom, saver);
            saver.nextLine();
        }

        System.out.println(saver.builder);

        saveToFile("create", saver.builder);
    }

    static class AtomsSaver implements AtomHandler {

        final StringBuilder builder = new StringBuilder();

        @Override
        public void handleNode(DBNode node) {
            builder
                    .append("(")
                    .append(node.type)
                    .append(" \"")
                    .append(node.value)
                    .append("\")");
        }

        @Override
        public void handleLinkBegin(DBLink link) {
            builder
                    .append("(")
                    .append(link.type)
                    .append(" ");
        }

        @Override
        public void handleLinkEnd(DBLink link) {
            builder.append(")");

        }

        void nextLine() {
            builder.append("\n");
        }
    }

    @Override
    public List<String> queryObjects(int iterations) {
        return new LinkedList<>();
    }

    @Override
    public String getName() {
        return "Evaluation";
    }
}
