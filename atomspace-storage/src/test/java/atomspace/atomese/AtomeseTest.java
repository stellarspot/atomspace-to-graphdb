package atomspace.atomese;

import org.junit.Assert;
import org.junit.Test;

import static atomspace.atomese.AtomeseBasic.*;

public class AtomeseTest {


    @Test
    public void test() {

        Assert.assertEquals("Concept('ball')", Concept("ball").toString());
        Assert.assertEquals("Inheritance(Concept('ball-1'), Concept('ball'))",
                Inheritance(Concept("ball-1"), Concept("ball")).toString());
    }
}