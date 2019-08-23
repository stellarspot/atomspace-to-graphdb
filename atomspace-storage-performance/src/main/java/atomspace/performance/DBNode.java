package atomspace.performance;

public class DBNode extends DBAtom {
    public final String value;

    public DBNode(long id, String type, String value) {
        super(id, type);
        this.value = value;
    }
}
