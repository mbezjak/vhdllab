package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link LibraryFileInfo}.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileInfoTest {

    private LibraryFileInfo file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(LibraryFileInfo.class, 1);
    }

    /**
     * FileResource is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        Integer libraryId = StubFactory.getStubValue("libraryId", 1);
        new LibraryFileInfo(null, libraryId);
    }

    /**
     * LibraryId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        FileResource resource = StubFactory.getStubValue("fileResource", 1);
        new LibraryFileInfo(resource, null);
    }

    /**
     * Library files are not limited by normal name format.
     */
    @Test
    public void constructor3() throws Exception {
        LibraryFileInfo another = StubFactory.create(LibraryFileInfo.class, 1);
        StubFactory.setProperty(another, "name", 302);
        Caseless caseless = StubFactory.getStubValue("name", 302);
        assertEquals("name not set.", caseless, another.getName());
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
        LibraryFileInfo another = StubFactory.create(LibraryFileInfo.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "libraryId", 1);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * LibraryIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        LibraryFileInfo another = new LibraryFileInfo(file);
        StubFactory.setProperty(another, "libraryId", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        LibraryFileInfo another = new LibraryFileInfo(file);
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
     * Simulate data tempering - libraryId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        StubFactory.setProperty(file, "libraryId", 300);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
