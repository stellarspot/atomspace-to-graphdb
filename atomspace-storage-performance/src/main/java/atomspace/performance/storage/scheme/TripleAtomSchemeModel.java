package atomspace.performance.storage.scheme;

import atomspace.performance.DBAtomSpace;
import atomspace.performance.storage.TripleAtomModel;
import atomspace.performance.triple.TripleGraph;

public abstract class TripleAtomSchemeModel extends TripleSchemeModel implements TripleAtomModel {

    protected final DBAtomSpace atomspace = new DBAtomSpace();

    public TripleAtomSchemeModel(String file, TripleGraph tripleGraph) {
        super(file, tripleGraph);
    }

}
