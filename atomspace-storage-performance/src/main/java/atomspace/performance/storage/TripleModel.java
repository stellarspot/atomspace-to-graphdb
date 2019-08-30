package atomspace.performance.storage;

import java.util.List;

public interface TripleModel {

    // Predicate: Alice likes ice-cream.
    // Triple (Alice, likes, ice-cream)
    void storeTriples();

    // What does Alice like?
    default List<String> queryObject() {
        return queryObjects(1);
    }

    List<String> queryObjects(int iterations);

    String getName();
}
