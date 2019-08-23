package atomspace.performance;

public abstract class DBAtom {
    public final long id;
    public final String type;

    public DBAtom(long id, String type) {
        this.id = id;
        this.type = type;
    }
}
