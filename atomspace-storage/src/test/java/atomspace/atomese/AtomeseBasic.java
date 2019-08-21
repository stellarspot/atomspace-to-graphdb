package atomspace.atomese;

import static atomspace.atomese.Atomese.*;

public class AtomeseBasic {

    public static Node Concept(String name) {
        return Node("Concept", name);
    }

    public static Link Inheritance(Atom... atoms) {
        return Link("Inheritance", atoms);
    }
}
