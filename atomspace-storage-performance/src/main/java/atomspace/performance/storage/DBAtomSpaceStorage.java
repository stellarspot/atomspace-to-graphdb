package atomspace.performance.storage;

import atomspace.performance.DBAtom;

public interface DBAtomSpaceStorage {

    void putAtoms(DBAtom... atoms);

}
