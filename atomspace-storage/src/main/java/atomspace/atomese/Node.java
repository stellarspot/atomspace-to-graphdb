package atomspace.atomese;

import java.util.Objects;

public class Node extends Atom {

    public final String name;

    public Node(String type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Node) {
            Node that = (Node) o;
            return Objects.equals(this.type, that.type) && Objects.equals(this.name, that.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type, name);
    }
}
