package atomspace.atomese;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Atomese {

    private static long GLOBAL_ID = 0;
    private static final Map<AtomWrapper, Long> ids = new HashMap<>();
    private static final Map<Long, Atom> atoms = new HashMap<>();


    public static Node Node(String type, String name) {

        Node node = new Node(-1, type, name);

        NodeWrapper key = new NodeWrapper(node);
        Long id = ids.get(key);

        if (id == null) {
            id = nextId();
            node = new Node(id, type, name);
            ids.put(key, id);
            atoms.put(id, node);
            return node;
        }

        return (Node) atoms.get(id);
    }

    public static Link Link(String type, Atom... atoms2) {
        Link link = new Link(-1, type, atoms2);

        LinkWrapper key = new LinkWrapper(link);
        Long id = ids.get(key);

        if (id == null) {
            id = nextId();
            link = new Link(id, type, atoms2);
            ids.put(key, id);
            atoms.put(id, link);
        }

        return (Link) atoms.get(id);
    }

    private static long nextId() {
        return GLOBAL_ID++;
    }

    private static abstract class AtomWrapper {
    }

    private static class NodeWrapper extends AtomWrapper {

        private final Node node;

        public NodeWrapper(Node node) {
            this.node = node;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o instanceof NodeWrapper) {
                Node node1 = node;
                Node node2 = ((NodeWrapper) o).node;
                return Objects.equals(node1.type, node2.type) && Objects.equals(node1.name, node2.name);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(node.type, node.name);
        }
    }

    private static class LinkWrapper extends AtomWrapper {

        private final Link link;

        public LinkWrapper(Link link) {
            this.link = link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o instanceof LinkWrapper) {
                Link link1 = link;
                Link link2 = ((LinkWrapper) o).link;
                return Objects.equals(link1.type, link2.type)
                        && Arrays.equals(link.values, link2.values);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return 31 * Objects.hashCode(link.type) + Arrays.hashCode(link.values);
        }
    }
}
