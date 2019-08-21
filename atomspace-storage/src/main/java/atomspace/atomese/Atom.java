package atomspace.atomese;

public abstract class Atom {
    public final long id;
    public final String type;

    public Atom(long id, String type) {
        this.id = id;
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Atom) {
            Atom that = (Atom) obj;
            return this.id == that.id;
        }

        return false;
    }
}
