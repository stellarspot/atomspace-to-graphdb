package atomspace.atomese;

public class Node extends Atom {

    public final String name;

    public Node(String type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type, name);
    }
}
