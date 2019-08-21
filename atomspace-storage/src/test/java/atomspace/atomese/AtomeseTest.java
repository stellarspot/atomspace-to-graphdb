package atomspace.atomese;

import org.junit.Assert;
import org.junit.Test;

import static atomspace.atomese.AtomeseBasic.*;

public class AtomeseTest {


    @Test
    public void testEquals() {

        Assert.assertEquals(
                Concept("ball-1"),
                Concept("ball-1"));

        Assert.assertNotEquals(
                Concept("ball-1"),
                Concept("ball-2"));

        Assert.assertEquals(
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")),
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")));

        Assert.assertNotEquals(
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")),
                Inheritance(
                        Concept("ball-2"),
                        Concept("ball")));

        Assert.assertNotEquals(
                List(
                        Concept("ball-1"),
                        Concept("ball")),
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")));
    }

    @Test
    public void testSame() {
        Assert.assertSame(
                Concept("ball-1"),
                Concept("ball-1"));

        Assert.assertSame(
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")),
                Inheritance(
                        Concept("ball-1"),
                        Concept("ball")));

    }

    @Test
    public void testToString() {

        Assert.assertEquals("Concept('ball')", Concept("ball").toString());
        Assert.assertEquals("Inheritance(Concept('ball-1'), Concept('ball'))",
                Inheritance(Concept("ball-1"), Concept("ball")).toString());
    }

}