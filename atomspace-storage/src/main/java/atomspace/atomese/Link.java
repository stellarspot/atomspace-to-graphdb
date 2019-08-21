package atomspace.atomese;

import java.util.Arrays;
import java.util.Objects;

public class Link extends Atom {

    public final Atom[] values;

    public Link(String type, Atom... values) {
        super(type);
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Link) {
            Link that = (Link) o;
            return Objects.equals(this.type, that.type)
                    && Arrays.equals(this.values, that.values);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(type) + Arrays.hashCode(values);
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
