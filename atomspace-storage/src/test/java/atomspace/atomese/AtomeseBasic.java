package atomspace.atomese;

import static atomspace.atomese.Atomese.Link;
import static atomspace.atomese.Atomese.Node;

public class AtomeseBasic {

    public static Node Concept(String name) {
        return Node("Concept", name);
    }

    public static Link Inheritance(Atom... atoms) {
        return Link("Inheritance", atoms);
    }

    public static Link List(Atom... atoms) {
        return Link("List", atoms);
    }
}
