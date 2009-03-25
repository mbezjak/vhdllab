package hr.fer.zemris.vhdllab.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class ValueObjectTestSupport {

    protected void assertEqualsAndHashCode(Object expected, Object actual) {
        assertTrue("not equal.", expected.equals(actual));
        assertTrue("hash code not same.", expected.hashCode() == actual
                .hashCode());
    }

    protected void assertNotEqualsAndHashCode(Object expected, Object actual) {
        assertFalse("equal when shouldn't.", expected.equals(actual));
        assertFalse("hash code same but shouldn't.",
                expected.hashCode() == actual.hashCode());
    }

    protected void basicEqualsTest(Object obj) {
        assertTrue("not equal with self.", obj.equals(obj));
        assertFalse("equal with null.", obj.equals(null));
        assertFalse("equal with a string object.", obj
                .equals("a string object"));
    }

    protected void toStringPrint(Object obj) {
        System.out.println(obj);
    }

}
