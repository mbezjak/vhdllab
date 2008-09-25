package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link LibraryFile} entity.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileTest {

    private LibraryFile file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(LibraryFile.class, 1);
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        Caseless name = StubFactory.getStubValue("name", 1);
        String data = StubFactory.getStubValue("data", 1);
        assertEquals("names not same.", name, file.getName());
        assertEquals("data not same.", data, file.getData());
    }

    /**
     * Library files are not limited by normal name format.
     */
    @Test
    public void constructor2() throws Exception {
        LibraryFile another = StubFactory.create(LibraryFile.class, 1);
        StubFactory.setProperty(another, "name", 302);
        Caseless caseless = StubFactory.getStubValue("name", 302);
        assertEquals("name not set.", caseless, another.getName());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        library.addFile(file);
        LibraryFile another = new LibraryFile(file);
        assertTrue("same reference.", file != another);
        assertFalse("equal.", file.equals(another));
        assertNotSame("hashCode same.", file.hashCode(), another.hashCode());
        assertNull("library reference copied.", another.getLibrary());
        assertTrue("original file's library is modified.", library == file
                .getLibrary());
        assertFalse("original file's library's files is modified.", library
                .getFiles().contains(another));
    }

    /**
     * Test equals with self, null, and non-LibraryFile.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only libraries and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        Library anotherLibrary = new Library(library);
        LibraryFile anotherLibraryFile = StubFactory.create(LibraryFile.class,
                2);
        library.addFile(file);
        anotherLibrary.addFile(anotherLibraryFile);
        StubFactory.setProperty(anotherLibraryFile, "name", 1);
        assertEquals("not equal.", file, anotherLibraryFile);
        assertEquals("hashCode not same.", file.hashCode(), anotherLibraryFile
                .hashCode());
    }

    /**
     * Libraries are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        Library anotherLibrary = StubFactory.create(Library.class, 2);
        LibraryFile anotherLibraryFile = new LibraryFile(file);
        library.addFile(file);
        anotherLibrary.addFile(anotherLibraryFile);
        assertFalse("equal.", file.equals(anotherLibraryFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherLibraryFile
                .hashCode());
    }

    /**
     * Libraries are different (one is null).
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        LibraryFile anotherLibraryFile = new LibraryFile(file);
        library.addFile(file);
        assertFalse("equal.", file.equals(anotherLibraryFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherLibraryFile
                .hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        Library anotherLibrary = new Library(library);
        LibraryFile anotherLibraryFile = StubFactory.create(LibraryFile.class,
                1);
        library.addFile(file);
        anotherLibrary.addFile(anotherLibraryFile);
        StubFactory.setProperty(anotherLibraryFile, "name", 2);
        assertFalse("equal.", file.equals(anotherLibraryFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherLibraryFile
                .hashCode());
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
    public void asString() {
        System.out.println(file);
    }

    @Test
    public void asStringInLibrary() throws Exception {
        Library library = StubFactory.create(Library.class, 1);
        library.addFile(file);
        System.out.println(file);
    }

}
