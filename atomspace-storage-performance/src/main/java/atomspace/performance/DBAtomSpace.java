package atomspace.performance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DBAtomSpace {

    private long id = 0;

    private Map<String, DBNode> nodes = new HashMap<>();
    private Map<String, DBLink> links = new HashMap<>();

    public DBNode getNode(String type, String value) {
        String key = String.format("%s:%s", type, value);
        return nodes.computeIfAbsent(key, (k) -> new DBNode(nextId(), type, value));
    }

    public DBLink getLink(String type, DBAtom... atoms) {
        StringBuilder b = new StringBuilder();
        b.append(type);
        Arrays.stream(atoms).forEach(atom -> b.append(":").append(atom.id));
        String key = b.toString();
        return links.computeIfAbsent(key, (k) -> new DBLink(nextId(), type, atoms));
    }

    private long nextId() {
        return id++;
    }
}
