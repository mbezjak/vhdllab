package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link LibraryFileDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileDAOImplTest {

    private static final Caseless NAME = new Caseless("simple_file_name");
    private static final String CONTENT = "<pref><value>schematic</value></pref>";
    private static final Integer NEW_LIBRARY_ID = Integer
            .valueOf(Integer.MAX_VALUE);
    private static final Caseless NEW_NAME = new Caseless("new_" + NAME);
    private static final String NEW_CONTENT = "library ieee;";

    private static LibraryFileDAO dao;
    private static LibraryDAO libraryDAO;
    private Library library;
    private LibraryFile file;

    @BeforeClass
    public static void initTestCase() throws DAOException {
        dao = new LibraryFileDAOImpl();
        libraryDAO = new LibraryDAOImpl();
        EntityManagerUtil.createEntityManagerFactory();
        destroyFiles();
    }

    @Before
    public void initEachTest() throws DAOException {
        initFiles();
        EntityManagerUtil.currentEntityManager();
    }

    private void initFiles() throws DAOException {
        library = new Library(new Caseless("library_name"));
        EntityManagerUtil.currentEntityManager();
        libraryDAO.save(library);
        EntityManagerUtil.closeEntityManager();

        file = new LibraryFile(NAME, CONTENT);
        library.addFile(file);
    }

    @After
    public void destroyEachTest() throws DAOException {
        EntityManagerUtil.closeEntityManager();
        destroyFiles();
    }

    private static void destroyFiles() throws DAOException {
        /*
         * Create a new entity manager for destroying files to isolate any
         * errors in a test
         */
        EntityManagerUtil.currentEntityManager();
        for (Library p : libraryDAO.getAll()) {
            libraryDAO.delete(p.getId());
        }
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * save a file then load it and see it they are the same
     */
    @Test
    public void saveAndLoad() throws DAOException {
        dao.save(file);
        LibraryFile loadedFile = dao.load(file.getId());
        assertEquals("file not equal after creating and loading it.", file,
                loadedFile);
        assertEquals("names are not same.", NAME, loadedFile.getName());
        assertEquals("libraries are not same.", library, loadedFile
                .getLibrary());
    }

    /**
     * File is null
     */
    @Test(expected = NullPointerException.class)
    public void save() throws DAOException {
        dao.save(null);
    }

    /**
     * Once file is persisted an ID is no longer null
     */
    @Test()
    public void save2() throws DAOException {
        assertNull("file has id set.", file.getId());
        dao.save(file);
        assertNotNull("file id wasn't set after creation.", file.getId());
    }

    /**
     * File content can be a part of update statement
     */
    @Test
    public void save3() throws Exception {
        dao.save(file);
        file.setData(NEW_CONTENT);
        dao.save(file);
        assertEquals("files not same after content was updated.", file, dao
                .load(file.getId()));
    }

    /**
     * non-existing library (can't cascade to persist a library)
     */
    @Test(expected = DAOException.class)
    public void save6() throws DAOException {
        Library newLibrary = new Library(new Caseless("new_library_name"));
        LibraryFile newFile = new LibraryFile(file);
        newLibrary.addFile(newFile);
        dao.save(newFile);
    }

    /**
     * If library is saved then file can be persisted
     */
    @Test
    public void save7() throws DAOException {
        Library newLibrary = new Library(new Caseless("new_library_name"));
        libraryDAO.save(newLibrary);
        LibraryFile newFile = new LibraryFile(file);
        newLibrary.addFile(newFile);
        dao.save(newFile);
        assertTrue("file doesn't exist.", dao.exists(newFile.getId()));
        assertTrue("file doesn't exist.", dao.exists(newLibrary.getId(),
                newFile.getName()));
        Library loadedLibrary = libraryDAO.load(newLibrary.getId());
        assertTrue("collection isn't updated.", loadedLibrary.getFiles()
                .contains(newFile));
    }

    /**
     * File name and library id are unique (i.e. form secondary key)
     */
    @Test
    public void save8() throws Exception {
        dao.save(file);
        LibraryFile newFile = new LibraryFile(file.getName(), CONTENT);
        library.addFile(newFile);
        try {
            dao.save(newFile);
            fail("Expected DAOException");
        } catch (DAOException e) {
            if (e.getStatusCode() != StatusCodes.DAO_ALREADY_EXISTS) {
                fail("Invalid status code in DAOException");
            }
        }
    }

    /**
     * Save a file with same library but different name
     */
    @Test
    public void save9() throws Exception {
        dao.save(file);
        LibraryFile newFile = new LibraryFile(NEW_NAME, CONTENT);
        library.addFile(newFile);
        dao.save(newFile);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Save a file with same name but different library
     */
    @Test
    public void save10() throws Exception {
        dao.save(file);
        Library newLibrary = new Library(new Caseless("new_library_name"));
        libraryDAO.save(newLibrary);
        LibraryFile newFile = new LibraryFile(NAME, CONTENT);
        newLibrary.addFile(newFile);
        dao.save(file);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * save a file then update it
     */
    @Test
    public void save11() throws DAOException {
        dao.save(file);
        file.setData("abc");
        dao.save(file);
        assertTrue("file doesn't exist.", dao.exists(file.getId()));
        assertEquals("file not updated.", file, dao.load(file.getId()));
    }

    /**
     * Id is null
     */
    @Test(expected = NullPointerException.class)
    public void load() throws Exception {
        dao.load(null);
    }

    /**
     * Save a file then delete it
     */
    @Test
    public void delete() throws Exception {
        dao.save(file);
        assertTrue("file not saved.", dao.exists(file.getId()));
        dao.delete(file.getId());
        assertFalse("file exists after it was deleted.", dao.exists(file
                .getId()));
        assertFalse("file exists after it was deleted.", dao.exists(library
                .getId(), file.getName()));
    }

    /**
     * Save a library and a file in same session then delete a file
     */
    @Test
    public void delete2() throws Exception {
        library = new Library(new Caseless("new_library_name"));
        libraryDAO.save(library);
        file = new LibraryFile(file);
        library.addFile(file);
        dao.save(file);
        assertTrue("file not saved.", dao.exists(file.getId()));
        dao.delete(file.getId());
        assertFalse("file exists after it was deleted.", dao.exists(file
                .getId()));
        assertFalse("file exists after it was deleted.", dao.exists(library
                .getId(), file.getName()));
    }

    /**
     * Delete a file in another session
     */
    @Test
    public void delete3() throws Exception {
        dao.save(file);
        assertTrue("file not saved.", dao.exists(file.getId()));
        EntityManagerUtil.closeEntityManager();
        EntityManagerUtil.currentEntityManager();
        dao.delete(file.getId());
        assertFalse("file exists after it was deleted.", dao.exists(file
                .getId()));
        assertFalse("file exists after it was deleted.", dao.exists(library
                .getId(), file.getName()));
    }

    /**
     * id is null
     */
    @Test(expected = NullPointerException.class)
    public void exists() throws DAOException {
        dao.exists((Integer) null);
    }

    /**
     * non-existing id
     */
    @Test
    public void exists2() throws DAOException {
        assertFalse("file exists.", dao.exists(Integer.MAX_VALUE));
    }

    /**
     * everything ok
     */
    @Test
    public void exists3() throws DAOException {
        dao.save(file);
        assertTrue("file doesn't exist.", dao.exists(file.getId()));
    }

    /**
     * library id is null
     */
    @Test(expected = NullPointerException.class)
    public void exists4() throws DAOException {
        dao.exists(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = NullPointerException.class)
    public void exists5() throws DAOException {
        dao.exists(file.getLibrary().getId(), null);
    }

    /**
     * non-existing library id and name
     */
    @Test
    public void exists6() throws Exception {
        assertFalse("file with unused library id exists.", dao.exists(
                NEW_LIBRARY_ID, NAME));
        assertFalse("file with unused name exists.", dao.exists(
                library.getId(), NEW_NAME));
    }

    /**
     * everything ok
     */
    @Test
    public void exists7() throws Exception {
        dao.save(file);
        assertTrue("file doesn't exists after creation.", dao.exists(file
                .getId()));
        assertTrue("file doesn't exists after creation.", dao.exists(library
                .getId(), file.getName()));
        assertTrue("file name is not case insensitive.", dao.exists(library
                .getId(), file.getName()));
    }

    /**
     * library id is null
     */
    @Test(expected = NullPointerException.class)
    public void findByName() throws DAOException {
        dao.findByName(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = NullPointerException.class)
    public void findByName2() throws DAOException {
        dao.findByName(file.getLibrary().getId(), null);
    }

    /**
     * non-existing library id
     */
    @Test
    public void findByName3() {
        try {
            dao.findByName(NEW_LIBRARY_ID, NAME);
            fail("Expected DAOException");
        } catch (DAOException e) {
            if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
                fail("Invalid status code in DAOException");
            }
        }
    }

    /**
     * non-existing name
     */
    @Test
    public void findByName4() {
        try {
            dao.findByName(file.getLibrary().getId(), NEW_NAME);
            fail("Expected DAOException");
        } catch (DAOException e) {
            if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
                fail("Invalid status code in DAOException");
            }
        }
    }

    /**
     * everything ok
     */
    @Test
    public void findByName5() throws Exception {
        dao.save(file);
        assertEquals("files are not same.", file, dao.findByName(library
                .getId(), file.getName()));
        assertEquals("file name is not case insensitive.", file, dao
                .findByName(library.getId(), file.getName()));
    }

}
