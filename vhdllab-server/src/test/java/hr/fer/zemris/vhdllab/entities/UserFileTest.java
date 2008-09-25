package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link UserFile}.
 * 
 * @author Miro Bezjak
 */
public class UserFileTest {

    private UserFile file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(UserFile.class, 1);
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        Caseless userId = StubFactory.getStubValue("userId", 1);
        Caseless name = StubFactory.getStubValue("name", 1);
        String data = StubFactory.getStubValue("data", 1);
        assertEquals("userIds not same.", userId, file.getUserId());
        assertEquals("names not same.", name, file.getName());
        assertEquals("data not same.", data, file.getData());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor() throws Exception {
        UserFile another = new UserFile(file);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("data not same.", file.getData(), another.getData());
        assertEquals("userIds not same.", file.getUserId(), another.getUserId());
    }

    /**
     * Test equals to UserFileInfo since UserFile doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        UserFileInfo info = new UserFileInfo(file);
        assertTrue("not same.", info.equals(file));
        assertTrue("not same.", file.equals(info));
        assertEquals("hashcode not same.", info.hashCode(), file.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(file);
        assertEquals("not same.", file, deserialized);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
