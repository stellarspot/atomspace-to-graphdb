package atomspace.generator;

import atomspace.performance.generator.Triple;
import atomspace.performance.generator.TripleGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class TripleGraphTest {

    @Test
    public void test() {

        int subjects = 4;
        int predicates = 3;
        int objects = 3;
        int predicatesPerObject = 2;

        TripleGraph tripleGraph = new TripleGraph(subjects, predicates, objects, predicatesPerObject);
        Set<Triple> triples = tripleGraph.getTriples();
        Assert.assertEquals(subjects, tripleGraph.getSubjects().size());
        Assert.assertEquals(objects, tripleGraph.getObjects().size());
        Assert.assertEquals(subjects * predicatesPerObject, triples.size());
    }
}
