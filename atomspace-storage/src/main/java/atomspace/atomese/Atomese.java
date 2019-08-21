package atomspace.atomese;

import java.util.HashMap;
import java.util.Map;

public class Atomese {

    private static long id = 0;
    private static final Map<Atom, IdAtomPair> map = new HashMap<>();

    public static Node Node(String type, String name) {
        return (Node) getUniqueAtom(new Node(type, name));
    }

    public static Link Link(String type, Atom... atoms) {
        return (Link) getUniqueAtom(new Link(type, atoms));
    }

    private static long nextId() {
        return id++;
    }

    public static Atom getUniqueAtom(Atom atom) {

        IdAtomPair pair = map.get(atom);

        if (pair == null) {
            map.put(atom, new IdAtomPair(atom));
            return atom;
        }
        return pair.atom;
    }


    static class IdAtomPair {
        final long id = nextId();
        final Atom atom;

        public IdAtomPair(Atom atom) {
            this.atom = atom;
        }
    }
}
