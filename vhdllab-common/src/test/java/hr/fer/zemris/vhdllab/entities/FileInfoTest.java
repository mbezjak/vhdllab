package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IFileInfoStub.PROJECT_ID;
import static hr.fer.zemris.vhdllab.entities.stub.IFileInfoStub.PROJECT_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE_2;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.entities.stub.FileResourceStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoTest {

    private FileInfoStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new FileInfoStub();
    }

    /**
     * Type is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new FileInfo(null, NAME, DATA, PROJECT_ID);
    }

    /**
     * Name is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new FileInfo(TYPE, null, DATA, PROJECT_ID);
    }

    /**
     * Data is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor3() throws Exception {
        new FileInfo(TYPE, NAME, null, PROJECT_ID);
    }

    /**
     * Project id is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor4() throws Exception {
        new FileInfo(TYPE, NAME, DATA, null);
    }

    /**
     * FileResource is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor5() throws Exception {
        new FileInfo(null, PROJECT_ID);
    }

    /**
     * ProjectId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor6() throws Exception {
        new FileInfo(new FileResourceStub(), null);
    }

    /**
     * By default info objects copy id and version fields.
     */
    @Test
    public void constructor7() throws Exception {
        FileInfo another = new FileInfo(new FileResourceStub(), PROJECT_ID);
        assertEquals("id not copied.", ID, another.getId());
        assertEquals("version not copied.", VERSION, another.getVersion());
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new FileInfo(null, true);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        FileInfo another = new FileInfo(file, true);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("projectIds not same.", file.getProjectId(), another
                .getProjectId());
    }

    /**
     * Test equals with self, null, and non-FileInfo.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only projectIds and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        FileInfoStub another = new FileInfoStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        another.setData(DATA_2);
        another.setType(TYPE_2);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * ProjectIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        FileInfoStub another = new FileInfoStub(file);
        another.setProjectId(PROJECT_ID_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        FileInfoStub another = new FileInfoStub(file);
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
     * Simulate data tempering - projectId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        file.setProjectId(null);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
