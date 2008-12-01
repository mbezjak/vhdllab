package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.UserFileStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link UserFile}.
 * 
 * @author Miro Bezjak
 */
public class UserFileTest {

    private UserFileStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new UserFileStub();
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        assertEquals("userIds not same.", USER_ID, file.getUserId());
        assertEquals("names not same.", NAME, file.getName());
        assertEquals("data not same.", DATA, file.getData());
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
        assertNull("id not null.", another.getId());
        assertNull("version not null.", another.getVersion());
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
