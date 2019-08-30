package atomspace.performance.storage.scheme;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
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
            DBAtom atom = TripleAtomModel.toAtom(atomspace, triple);
            atoms.add(atom);
        }
    }

}
