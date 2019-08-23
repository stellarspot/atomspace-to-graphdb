package atomspace.performance;

public class DBLink extends DBAtom {
    final DBAtom[] values;

    public DBLink(long id, String type, DBAtom... values) {
        super(id, type);
        this.values = values;
    }
}
