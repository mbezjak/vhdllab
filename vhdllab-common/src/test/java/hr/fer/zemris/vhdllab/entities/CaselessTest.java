package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import hr.fer.zemris.vhdllab.entities.Caseless;

import java.lang.reflect.Field;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

/**
 * A test case for {@link Caseless}.
 * 
 * @author Miro Bezjak
 */
public class CaselessTest {

    private static final String EMPTY_STRING = "";
    private static final String STRING = "a string object";
    private static final Caseless CASELESS = new Caseless(STRING);

    /**
     * Test empty caseless identity and emptiness.
     */
    @Test
    public void empty() {
        Caseless first = Caseless.EMPTY;
        Caseless second = Caseless.EMPTY;
        assertTrue("not identical.", first == second);
        assertEquals("not empty.", EMPTY_STRING, first.toString());
    }

    /**
     * Default constructor constructs empty caseless.
     */
    @Test
    public void constructorDefault() {
        Caseless caseless = new Caseless();
        assertEquals("not empty.", EMPTY_STRING, caseless.toString());
    }

    /**
     * Constructor constructs caseless from given string.
     */
    @Test
    public void constructor() {
        assertEquals("wrong inner string.", STRING, CASELESS.toString());
    }

    /**
     * Null argument.
     */
    @Test(expected = NullPointerException.class)
    public void constructorNull() {
        new Caseless(null);
    }

    /**
     * Test equals with self, null and non-caseless object.
     */
    @Test
    public void testEquals() {
        assertTrue("not equal with self.", CASELESS.equals(CASELESS));
        assertFalse("equal to null.", CASELESS.equals(null));
        assertFalse("equal to non-caseless object.", CASELESS
                .equals("a string object"));
    }

    /**
     * Test equals hashCode and compareTo methods for non-english strings.
     */
    @Test
    public void equalsHashCodeAndCompareTo() {
        Caseless first = new Caseless("čćžšđ");
        Caseless second = new Caseless("ČĆŽŠĐ");
        assertTrue("not equal.", first.equals(second));
        assertEquals("not equal by compareto.", 0, first.compareTo(second));
        assertEquals("hashcode not same.", first.hashCode(), second.hashCode());
    }

    /**
     * Test equals hashCode and compareTo methods.
     */
    @Test
    public void equalsHashCodeAndCompareTo2() {
        Caseless first = new Caseless(STRING);
        Caseless second = new Caseless(STRING);
        assertTrue("not equal.", first.equals(second));
        assertEquals("not equal by compareto.", 0, first.compareTo(second));
        assertEquals("hashcode not same.", first.hashCode(), second.hashCode());
    }

    /**
     * Strings are not equal.
     */
    @Test
    public void equalsHashCodeAndCompareTo3() {
        Caseless first = new Caseless("first string");
        Caseless second = new Caseless("second string");
        assertFalse("equal.", first.equals(second));
        assertNotSame("equal by compareto.", 0, first.compareTo(second));
        assertNotSame("same hashcode.", first.hashCode(), second.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(CASELESS);
        assertEquals("not same.", CASELESS, deserialized);
    }

    /**
     * Simulate data tempering - name is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        Field field = CASELESS.getClass().getDeclaredField("inner");
        field.setAccessible(true);
        field.set(CASELESS, null);
        SerializationUtils.clone(CASELESS);
    }

    /**
     * Test caseless length.
     */
    @Test
    public void length() {
        String string = "a string";
        Caseless caseless = new Caseless(string);
        assertEquals("length not same.", string.length(), caseless.length());
    }

}
