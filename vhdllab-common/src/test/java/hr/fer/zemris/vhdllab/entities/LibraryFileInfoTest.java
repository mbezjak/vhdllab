package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_NOT_CORRECTLY_FORMATTED;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.ILibraryFileInfoStub.LIBRARY_ID;
import static hr.fer.zemris.vhdllab.entities.stub.ILibraryFileInfoStub.LIBRARY_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileResourceStub;
import hr.fer.zemris.vhdllab.entities.stub.LibraryFileInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link LibraryFileInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileInfoTest {

    private LibraryFileInfoStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new LibraryFileInfoStub();
    }

    /**
     * FileResource is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new LibraryFileInfo(null, LIBRARY_ID);
    }

    /**
     * LibraryId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new LibraryFileInfo(new FileResourceStub(), null);
    }

    /**
     * Library files are not limited by normal name format.
     */
    @Test
    public void constructor3() throws Exception {
        LibraryFileInfoStub another = new LibraryFileInfoStub();
        another.setName(NAME_NOT_CORRECTLY_FORMATTED);
        assertEquals("name not set.", NAME_NOT_CORRECTLY_FORMATTED, another
                .getName());
    }

    /**
     * LibraryFileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new LibraryFileInfo(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        LibraryFileInfo another = new LibraryFileInfo(file);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("libraryIds not same.", file.getLibraryId(), another
                .getLibraryId());
    }

    /**
     * Test equals with self, null, and non-LibraryFileInfo.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only libraryIds and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        LibraryFileInfoStub another = new LibraryFileInfoStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        another.setData(DATA_2);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * LibraryIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        LibraryFileInfoStub another = new LibraryFileInfoStub(file);
        another.setLibraryId(LIBRARY_ID_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        LibraryFileInfoStub another = new LibraryFileInfoStub(file);
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
     * Simulate data tempering - libraryId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        file.setLibraryId(null);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
