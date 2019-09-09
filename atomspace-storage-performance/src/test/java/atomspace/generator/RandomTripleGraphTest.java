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
        int statements = 5;

        TripleGraph tripleGraph = new RandomTripleGraph(subjects, predicates, objects, statements);
        Set<Triple> triples = tripleGraph.getTriples();
        Assert.assertEquals(subjects, tripleGraph.getSubjects().size());
        Assert.assertEquals(objects, tripleGraph.getObjects().size());
        Assert.assertEquals(statements, triples.size());
    }
}
