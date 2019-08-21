package atomspace.atomese;

public class Node extends Atom {

    public final String name;

    public Node(long id, String type, String name) {
        super(id, type);
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type, name);
    }
}
