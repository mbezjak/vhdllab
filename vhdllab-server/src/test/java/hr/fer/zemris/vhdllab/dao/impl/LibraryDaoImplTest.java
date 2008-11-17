package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.PreparedStatementCallback;

/**
 * Tests for {@link LibraryDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryDaoImplTest extends AbstractDaoSupport {

    @Autowired
    private LibraryFileDao fileDAO;
    @Autowired
    private LibraryDao dao;
    private Library library;
    private LibraryFile file;

    @Before
    public void initEachTest() {
        library = new Library(NAME);
        file = new LibraryFile(NAME, DATA);
        library.addFile(file);
    }

    /**
     * Cascade to saving files when creating a library.
     */
    @Test
    public void saveCascade() {
        dao.save(library);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
    }

    /**
     * Library name is unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        setupLibrary(library);
        Library newLibrary = new Library(NAME);
        dao.save(newLibrary);
    }

    /**
     * Save a library with different name.
     */
    @Test
    public void saveDifferentName() {
        setupLibrary(library);
        Library newLibrary = new Library(NAME_2);
        dao.save(newLibrary);
        assertEquals("libraries are not same.", newLibrary, dao.load(newLibrary
                .getId()));
    }

    /**
     * Name is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNameCaseInsensitive() {
        setupLibrary(library);
        Library newLibrary = new Library(NAME_OPPOSITE_CASE);
        dao.save(newLibrary);
    }

    /**
     * add a file after a library is saved.
     */
    @Test
    public void saveAddFile() {
        dao.save(library);
        LibraryFile newFile = new LibraryFile(NAME_2, DATA_2);
        library.addFile(newFile);
        dao.save(library);

        Library loadedLibrary = dao.load(library.getId());
        assertNotNull("file not saved.", fileDAO.load(newFile.getId()));
        assertNotNull("file not saved.", fileDAO.findByName(loadedLibrary
                .getId(), newFile.getName()));
        assertEquals("libraries not equal.", library, loadedLibrary);
        assertTrue("library doesn't contain a new file.", loadedLibrary
                .getFiles().contains(newFile));
    }

    /**
     * remove a file from a library then save library.
     */
    @Test
    public void saveRemoveFile() {
        dao.save(library);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
        library.removeFile(file);
        assertNull("file not removed from collection.", file.getLibrary());
        assertFalse("file still in collection.", library.getFiles().contains(
                file));
        dao.save(library);
        assertNull("file still exists.", fileDAO.load(file.getId()));
        assertFalse("library still contains a file.", library.getFiles()
                .contains(file));
    }

    /**
     * name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullName() {
        dao.findByName(null);
    }

    /**
     * non-existing name
     */
    @Test
    public void findByNameNonExistingName() {
        Library newLibrary = dao.findByName(NAME_2);
        assertNull("library already exists.", newLibrary);
    }

    /**
     * everything ok
     */
    @Test
    public void findByName() {
        setupLibrary(library);
        assertEquals("library not found.", library, dao.findByName(library
                .getName()));
        assertEquals("library name is not case insensitive.", library, dao
                .findByName(NAME_OPPOSITE_CASE));
    }

    /**
     * everything ok
     */
    @Test
    public void getAll() {
        setupLibrary(library);
        List<Library> expected = new ArrayList<Library>(1);
        expected.add(library);
        assertEquals("lists not equal.", expected, dao.getAll());
    }

    /**
     * no libraries
     */
    @Test
    public void getAllEmpty() {
        assertEquals("List not empty.", Collections.emptyList(), dao.getAll());
    }

    private void setupLibrary(final Library library) {
        String query = createQuery("libraries", "id, version, name",
                "null, 0, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, library.getName().toString());
                return ps.execute();
            }
        });
    }

}
