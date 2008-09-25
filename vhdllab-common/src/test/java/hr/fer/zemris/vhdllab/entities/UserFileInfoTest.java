package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link UserFileInfo}.
 * 
 * @author Miro Bezjak
 */
public class UserFileInfoTest {

    private static final Caseless NAME = StubFactory.getStubValue("name", 1);
    private static final String DATA = StubFactory.getStubValue("data", 1);
    private UserFileInfo file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(UserFileInfo.class, 1);
    }

    /**
     * UserId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new UserFileInfo(null, NAME, DATA);
    }

    /**
     * UserId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        Caseless userId = StubFactory.getStubValue("userId", 301);
        new UserFileInfo(userId, NAME, DATA);
    }

    /**
     * User files are not limited by normal name format.
     */
    @Test
    public void constructor3() throws Exception {
        Caseless caseless = StubFactory.getStubValue("name", 302);
        Caseless userId = StubFactory.getStubValue("userId", 1);
        UserFileInfo another = new UserFileInfo(userId, caseless, DATA);
        assertEquals("name not set.", caseless, another.getName());
    }

    /**
     * UserFileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new UserFileInfo((UserFileInfo) null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        UserFileInfo another = new UserFileInfo(file);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("userIds not same.", file.getUserId(), another.getUserId());
    }

    /**
     * Test equals with self, null, and non-UserFileInfo.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only userIds and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        UserFileInfo another = StubFactory.create(UserFileInfo.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "userId", 1);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        UserFileInfo another = new UserFileInfo(file);
        StubFactory.setProperty(another, "userId", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        UserFileInfo another = new UserFileInfo(file);
        StubFactory.setProperty(another, "name", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(file);
        assertEquals("not same.", file, deserialized);
    }

    /**
     * Simulate data tempering - userId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        StubFactory.setProperty(file, "userId", 300);
        SerializationUtils.clone(file);
    }

    /**
     * Simulate data tempering - userId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        StubFactory.setProperty(file, "userId", 301);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
