package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_NOT_CORRECTLY_FORMATTED;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_TOO_LONG;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.UserFileInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link UserFileInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class UserFileInfoTest {

    private UserFileInfoStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new UserFileInfoStub();
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
        new UserFileInfo(USER_ID_TOO_LONG, NAME, DATA);
    }

    /**
     * User files are not limited by normal name format.
     */
    @Test
    public void constructor3() throws Exception {
        Caseless name = NAME_NOT_CORRECTLY_FORMATTED;
        UserFileInfo another = new UserFileInfo(USER_ID, name, DATA);
        assertEquals("name not set.", name, another.getName());
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
        assertEquals("id not copied.", ID, another.getId());
        assertEquals("version not copied.", VERSION, another.getVersion());
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
        UserFileInfoStub another = new UserFileInfoStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        another.setData(DATA_2);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        UserFileInfoStub another = new UserFileInfoStub(file);
        another.setUserId(USER_ID_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        UserFileInfoStub another = new UserFileInfoStub(file);
        another.setName(NAME_2);
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
        file.setUserId(null);
        SerializationUtils.clone(file);
    }

    /**
     * Simulate data tempering - userId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        file.setUserId(USER_ID_TOO_LONG);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
