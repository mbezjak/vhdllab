package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.ILibraryFileInfoStub.LIBRARY_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link LibraryFileDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileDaoImplTest extends AbstractDaoSupport {

    @Autowired
    private LibraryFileDao dao;
    @Autowired
    private LibraryDao libraryDAO;
    private Library library;
    private LibraryFile file;

    @Before
    public void initEachTest() {
        library = new Library(NAME);
        libraryDAO.save(library);

        file = new LibraryFile(NAME, DATA);
        library.addFile(file);
    }

    /**
     * save a file then load it and see it they are the same.
     */
    @Test
    public void saveAndLoad() {
        dao.save(file);
        LibraryFile loadedFile = dao.load(file.getId());
        assertEquals("file not equal after creating and loading it.", file,
                loadedFile);
        assertEquals("libraries are not same.", library, loadedFile
                .getLibrary());
    }

    /**
     * non-existing library (can't cascade to persist a library).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNonExistingLibrary() {
        Library newLibrary = new Library(NAME_2);
        LibraryFile newFile = new LibraryFile(file);
        newLibrary.addFile(newFile);
        dao.save(newFile);
    }

    /**
     * If library is saved then file can be persisted.
     */
    @Test
    public void saveExistingLibrary() {
        Library newLibrary = new Library(new Caseless("new_library_name"));
        libraryDAO.save(newLibrary);
        LibraryFile newFile = new LibraryFile(file.getName(), file.getData());
        newLibrary.addFile(newFile);
        dao.save(newFile);
        assertEquals("files not same.", newFile, dao.load(newFile.getId()));
        assertEquals("files not same.", newFile, dao.findByName(newLibrary
                .getId(), newFile.getName()));
        Library loadedLibrary = libraryDAO.load(newLibrary.getId());
        assertTrue("collection isn't updated.", loadedLibrary.getFiles()
                .contains(newFile));
    }

    /**
     * File name and library id are unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        dao.save(file);
        LibraryFile newFile = new LibraryFile(file.getName(), DATA);
        library.addFile(newFile);
        dao.save(newFile);
    }

    /**
     * Save a file with same library but different name.
     */
    @Test
    public void saveDifferentName() {
        dao.save(file);
        LibraryFile newFile = new LibraryFile(NAME_2, DATA);
        library.addFile(newFile);
        dao.save(newFile);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Name is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNameCaseInsensitive() {
        dao.save(file);
        LibraryFile newFile = new LibraryFile(NAME_OPPOSITE_CASE, DATA);
        library.addFile(newFile);
        dao.save(newFile);
    }

    /**
     * Save a file with same name but different library.
     */
    @Test
    public void saveDifferentLibrary() {
        dao.save(file);
        Library newLibrary = new Library(NAME_2);
        libraryDAO.save(newLibrary);
        LibraryFile newFile = new LibraryFile(NAME, DATA);
        newLibrary.addFile(newFile);
        dao.save(file);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Save a library and a file in same session then delete a file
     */
    @Test
    public void saveAndDeleteInSameSession() {
        Library newLibrary = new Library(NAME_2);
        libraryDAO.save(newLibrary);
        LibraryFile newFile = new LibraryFile(file.getName(), file.getData());
        newLibrary.addFile(newFile);
        dao.save(newFile);
        assertEquals("file not saved.", newFile, dao.load(newFile.getId()));
        dao.delete(newFile);
        assertNull("file exists after it was deleted.", dao.load(newFile
                .getId()));
        assertNull("file exists after it was deleted.", dao.findByName(
                newLibrary.getId(), newFile.getName()));
    }

    /**
     * library id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullLibraryId() {
        dao.findByName(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullName() {
        dao.findByName(file.getLibrary().getId(), null);
    }

    /**
     * non-existing library id
     */
    @Test
    public void findByNameNonExistingLibraryId() {
        assertNull("file already exists.", dao.findByName(LIBRARY_ID_2, NAME));
    }

    /**
     * non-existing name
     */
    @Test
    public void findByNameNonExistingName() {
        assertNull("file already exists.", dao.findByName(file.getLibrary()
                .getId(), NAME_2));
    }

    /**
     * everything ok
     */
    @Test
    public void findByName() {
        dao.save(file);
        assertEquals("files are not same.", file, dao.findByName(library
                .getId(), file.getName()));
        assertEquals("file name is not case insensitive.", file, dao
                .findByName(library.getId(), NAME_OPPOSITE_CASE));
    }

}
