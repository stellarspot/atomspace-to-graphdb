package atomspace.performance.generator;

import atomspace.performance.DBAtom;

import java.util.List;

public interface DBAtomsGenerator {
    List<DBAtom> getAtoms();
}
