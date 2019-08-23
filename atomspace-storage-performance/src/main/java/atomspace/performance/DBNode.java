package atomspace.performance;

public class DBNode extends DBAtom {

    public final String value;

    DBNode(long id, String type, String value) {
        super(id, type);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Node(%d, %s, %s)", id, type, value);
    }
}
