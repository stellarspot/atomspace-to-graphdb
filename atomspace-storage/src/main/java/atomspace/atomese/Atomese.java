package atomspace.atomese;

public class Atomese {

    public static Node Node(String type, String name) {
        return new Node(type, name);
    }

    public static Link Link(String type, Atom... atoms) {
        return new Link(type, atoms);
    }
}
