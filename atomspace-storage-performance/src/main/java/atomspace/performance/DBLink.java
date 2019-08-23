package atomspace.performance;

public class DBLink extends DBAtom {

    final DBAtom[] atoms;

    DBLink(long id, String type, DBAtom... atoms) {
        super(id, type);
        this.atoms = atoms;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Link(")
                .append(id)
                .append(", ")
                .append(type);

        for (DBAtom atom : atoms) {
            builder
                    .append(", ")
                    .append(atom);
        }

        builder.append(")");

        return builder.toString();
    }
}
