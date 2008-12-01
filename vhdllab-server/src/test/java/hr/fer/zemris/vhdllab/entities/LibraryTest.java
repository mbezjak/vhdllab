package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.LibraryFileStub;
import hr.fer.zemris.vhdllab.entities.stub.LibraryFileStub2;
import hr.fer.zemris.vhdllab.entities.stub.LibraryStub;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link Library} entity.
 * 
 * @author Miro Bezjak
 */
public class LibraryTest {

    private LibraryStub library;

    @Before
    public void initEachTest() throws Exception {
        library = new LibraryStub();
        library.addFile(new LibraryFileStub());
    }

    /**
     * Name is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new Library((Caseless) null);
    }

    /**
     * Files isn't null after creation.
     */
    @Test
    public void constructor2() throws Exception {
        Library another = new Library(NAME);
        assertNotNull("files is null.", another.getFiles());
        assertTrue("files not empty.", another.getFiles().isEmpty());
    }

    /**
     * Library is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new Library((Library) null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        Set<LibraryFile> files = library.getFiles();
        Library another = new Library(library);
        assertTrue("same reference.", library != another);
        assertEquals("not equal.", library, another);
        assertEquals("hashCode not same.", library.hashCode(), another
                .hashCode());
        assertEquals("files are copied.", Collections.emptySet(), another
                .getFiles());
        assertNotNull("original file reference is missing.", library.getFiles());
        assertEquals("original files has been modified.", files, library
                .getFiles());
        assertNull("id not null.", another.getId());
        assertNull("version not null.", another.getVersion());
    }

    /**
     * Get files returns a modifiable version. (users are discouraged to use
     * direct files reference to add or remove a file)
     */
    @Test
    public void getLibraryFiles() throws Exception {
        Set<LibraryFile> files = new HashSet<LibraryFile>(library.getFiles());
        LibraryFile anotherFile = new LibraryFileStub2();
        library.getFiles().add(anotherFile);
        files.add(anotherFile);
        assertTrue("file not added.", library.getFiles().contains(anotherFile));
        assertEquals("file size not same.", files.size(), library.getFiles()
                .size());
        assertEquals("files not same.", files, library.getFiles());
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void addLibraryFile() {
        library.addFile(null);
    }

    /**
     * Add a file.
     */
    @Test
    public void addLibraryFile2() throws Exception {
        Set<LibraryFile> files = new HashSet<LibraryFile>(library.getFiles());
        LibraryFile anotherFile = new LibraryFileStub2();
        library.addFile(anotherFile);
        files.add(anotherFile);
        assertTrue("file not added.", library.getFiles().contains(anotherFile));
        assertEquals("file size not same.", files.size(), library.getFiles()
                .size());
        assertEquals("files not same.", files, library.getFiles());
    }

    /**
     * Add a file that is already in that library.
     */
    @Test
    public void addLibraryFile3() throws Exception {
        Set<LibraryFile> files = new HashSet<LibraryFile>(library.getFiles());
        LibraryFile file = library.getFiles().iterator().next();
        library.addFile(file);
        assertEquals("files not same.", files, library.getFiles());
        assertEquals("files is changed.", 1, library.getFiles().size());
    }

    /**
     * Add a file that is already in another library.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addLibraryFile4() throws Exception {
        Library another = new Library(library);
        LibraryFile file = library.getFiles().iterator().next();
        another.addFile(file);
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void removeLibraryFile() throws Exception {
        library.removeFile(null);
    }

    /**
     * Remove a file.
     */
    @Test
    public void removeLibraryFile2() throws Exception {
        LibraryFile file = library.getFiles().iterator().next();
        library.removeFile(file);
        assertEquals("files not empty.", Collections.emptySet(), library
                .getFiles());
        assertNull("library isn't set to null.", file.getLibrary());
    }

    /**
     * Remove a file that doesn't belong to any library.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeLibraryFile3() throws Exception {
        library.removeFile(new LibraryFileStub2());
    }

    /**
     * Remove a file that belongs to another library.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeLibraryFile4() throws Exception {
        LibraryFile file = library.getFiles().iterator().next();
        Library another = new Library(library);
        another.removeFile(file);
    }

    /**
     * Test equals to LibraryInfo since Library doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        LibraryInfo info = new LibraryInfo(library);
        assertTrue("not same.", info.equals(library));
        assertTrue("not same.", library.equals(info));
        assertEquals("hashcode not same.", info.hashCode(), library.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(library);
        assertEquals("not same.", library, deserialized);
    }

    /**
     * Simulate data tempering - files is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        library.setFiles(null);
        SerializationUtils.clone(library);
    }

    @Test
    public void asString() {
        System.out.println(library);
    }

}
