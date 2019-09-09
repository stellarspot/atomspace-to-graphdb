package atomspace.performance.triple;

import java.util.HashSet;
import java.util.Set;

public abstract class TripleGraph {

    protected final Set<String> subjects = new HashSet<>();
    protected final Set<String> objects = new HashSet<>();
    protected final Set<String> predicates = new HashSet<>();
    protected final Set<Triple> triples = new HashSet<>();


    public Set<String> getSubjects() {
        return subjects;
    }

    public Set<String> getObjects() {
        return objects;
    }

    public Set<String> getPredicates() {
        return predicates;
    }

    public Set<Triple> getTriples() {
        return triples;
    }

    public String getStatistics() {
        return String.format("subjects: %d, predicates: %d, objects: %d, triples: %d%n",
                subjects.size(), predicates.size(), objects.size(), triples.size());
    }

    protected final void addTriple(Triple triple) {
        subjects.add(triple.subject);
        predicates.add(triple.predicate);
        objects.add(triple.object);
        triples.add(triple);
    }
}
