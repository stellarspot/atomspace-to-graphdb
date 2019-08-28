package atomspace.performance.triple;

import java.util.Comparator;
import java.util.Objects;

public class Triple implements Comparable<Triple> {
    public final String subject;
    public final String predicate;
    public final String object;

    public Triple(int i, int j, int k) {
        this("subject-" + i, "predicate-" + j, "object-" + k);
    }

    public Triple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triple)) return false;
        Triple triple = (Triple) o;
        return Objects.equals(subject, triple.subject) &&
                Objects.equals(predicate, triple.predicate) &&
                Objects.equals(object, triple.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, predicate, object);
    }

    @Override
    public int compareTo(Triple o) {
        return Comparator.comparing(Triple::getSubject)
                .thenComparing(Triple::getPredicate)
                .thenComparing(Triple::getObject)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", subject, predicate, object);
    }
}
