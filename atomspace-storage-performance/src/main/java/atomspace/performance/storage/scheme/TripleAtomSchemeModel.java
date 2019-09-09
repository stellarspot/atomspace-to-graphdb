package atomspace.performance.storage.scheme;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.storage.TripleAtomModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;

import java.util.ArrayList;
import java.util.List;

public abstract class TripleAtomSchemeModel extends TripleSchemeModel implements TripleAtomModel {

    protected final DBAtomSpace atomspace = new DBAtomSpace();
    protected List<DBAtom> atoms = new ArrayList<>();

    public TripleAtomSchemeModel(String file, TripleGraph tripleGraph) {
        super(file, tripleGraph);
        for (Triple triple : tripleGraph.getTriples()) {
            DBAtom atom = toAtom(triple);
            atoms.add(atom);
        }
    }

    protected abstract DBAtom toAtom(Triple triple);

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
}
