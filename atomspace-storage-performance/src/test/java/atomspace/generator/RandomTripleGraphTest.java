package atomspace.generator;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.TripleGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class RandomTripleGraphTest {

    @Test
    public void test() {

        int subjects = 4;
        int predicates = 3;
        int objects = 3;
        int predicatesPerObject = 2;

        TripleGraph tripleGraph = new RandomTripleGraph(subjects, predicates, objects, predicatesPerObject);
        Set<Triple> triples = tripleGraph.getTriples();
        Assert.assertEquals(subjects, tripleGraph.getSubjects().size());
        Assert.assertEquals(objects, tripleGraph.getObjects().size());
        Assert.assertEquals(subjects * predicatesPerObject, triples.size());
    }
}
