package atomspace.atomese;

public class Link extends Atom {
    public final Atom[] values;

    public Link(String type, Atom... values) {
        super(type);
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(type).append("(");
        boolean first = true;
        for (Atom atom : values) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(atom);
        }
        builder.append(")");

        return builder.toString();
    }
}
