package atomspace.generator;

import atomspace.performance.DBAtom;
import atomspace.performance.generator.DBAtomsPredicatesGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DBAtomsPredicatesGeneratorTest {

    @Test
    public void test() {

        DBAtomsPredicatesGenerator generator = new DBAtomsPredicatesGenerator(4, 3, 3, 2);

//        List<Triple> triples = generator.getTriples();
//        Collections.sort(triples);
//
//        for (Triple triple : triples) {
//            System.out.println(triple);
//        }

        List<DBAtom> atoms = generator.getAtoms();
        Assert.assertEquals(8, atoms.size());
    }
}
