package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link FileDAOImpl}.
 *
 * @author Miro Bezjak
 */
public class FileDAOImplTest {

    private static final Caseless NAME = new Caseless("simple_file_name");
    private static final FileType TYPE = FileType.SCHEMA;
    private static final Caseless USER_ID = new Caseless("user.identifier");
    private static final String CONTENT = "<pref><value>schematic</value></pref>";
    private static final Integer NEW_PROJECT_ID = Integer.valueOf(Integer.MAX_VALUE);
    private static final Caseless NEW_NAME = new Caseless("new_" + NAME);
    private static final String NEW_CONTENT = "library ieee;";

    private static FileDAO dao;
    private static ProjectDAO projectDAO;
    private Project project;
    private File file;

    @BeforeClass
    public static void initTestCase() throws DAOException {
        dao = new FileDAOImpl();
        projectDAO = new ProjectDAOImpl();
        EntityManagerUtil.createEntityManagerFactory();
        destroyFiles();
    }

    @Before
    public void initEachTest() throws DAOException {
        initFiles();
        EntityManagerUtil.currentEntityManager();
    }

    private void initFiles() throws DAOException {
        project = new Project(USER_ID, new Caseless("project_name"));
        EntityManagerUtil.currentEntityManager();
        projectDAO.save(project);
        EntityManagerUtil.closeEntityManager();

        file = new File(TYPE, NAME, CONTENT);
        project.addFile(file);
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
        for (Project p : projectDAO.findByUser(USER_ID)) {
            projectDAO.delete(p.getId());
        }
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * save a file then load it and see it they are the same
     */
    @Test
    public void saveAndLoad() throws DAOException {
        dao.save(file);
        File loadedFile = dao.load(file.getId());
        assertEquals("file not equal after creating and loading it.", file,
                loadedFile);
        assertEquals("names are not same.", NAME, loadedFile.getName());
        assertEquals("projects are not same.", project, loadedFile.getProject());
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
     * non-existing project (can't cascade to persist a project)
     */
    @Test(expected = DAOException.class)
    public void save4() throws DAOException {
        Project newProject = new Project(USER_ID, new Caseless("new_project_name"));
        File newFile = new File(file);
        newProject.addFile(newFile);
        dao.save(newFile);
    }

    /**
     * If project is saved then file can be persisted
     */
    @Test
    public void save5() throws DAOException {
        Project newProject = new Project(USER_ID, new Caseless("new_project_name"));
        projectDAO.save(newProject);
        File newFile = new File(file);
        newProject.addFile(newFile);
        dao.save(newFile);
        assertTrue("file doesn't exist.", dao.exists(newFile.getId()));
        assertTrue("file doesn't exist.", dao.exists(newProject.getId(),
                newFile.getName()));
        Project loadedProject = projectDAO.load(newProject.getId());
        assertTrue("collection isn't updated.", loadedProject.getFiles()
                .contains(newFile));
    }

    /**
     * File name and project id are unique (i.e. form secondary key)
     */
    @Test
    public void save6() throws Exception {
        dao.save(file);
        File newFile = new File(TYPE, file.getName(), CONTENT);
        project.addFile(newFile);
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
     * Save a file with same project but different name
     */
    @Test
    public void save7() throws Exception {
        dao.save(file);
        File newFile = new File(TYPE, NEW_NAME, CONTENT);
        project.addFile(newFile);
        dao.save(newFile);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Save a file with same name but different project
     */
    @Test
    public void save8() throws Exception {
        dao.save(file);
        Project newProject = new Project(USER_ID, new Caseless("new_project_name"));
        projectDAO.save(newProject);
        File newFile = new File(TYPE, NAME, CONTENT);
        newProject.addFile(newFile);
        dao.save(file);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * save a file then update it
     */
    @Test
    public void save9() throws DAOException {
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
        assertFalse("file exists after it was deleted.", dao.exists(project
                .getId(), file.getName()));
    }

    /**
     * Save a project and a file in same session then delete a file
     */
    @Test
    public void delete2() throws Exception {
        project = new Project(project.getUserId(), new Caseless("new_project_name"));
        projectDAO.save(project);
        file = new File(file);
        project.addFile(file);
        dao.save(file);
        assertTrue("file not saved.", dao.exists(file.getId()));
        dao.delete(file.getId());
        assertFalse("file exists after it was deleted.", dao.exists(file
                .getId()));
        assertFalse("file exists after it was deleted.", dao.exists(project
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
        assertFalse("file exists after it was deleted.", dao.exists(project
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
     * project id is null
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
        dao.exists(file.getProject().getId(), null);
    }

    /**
     * non-existing project id and name
     */
    @Test
    public void exists6() throws Exception {
        assertFalse("file with unused project id exists.", dao.exists(
                NEW_PROJECT_ID, NAME));
        assertFalse("file with unused name exists.", dao.exists(
                project.getId(), NEW_NAME));
    }

    /**
     * everything ok
     */
    @Test
    public void exists7() throws Exception {
        dao.save(file);
        assertTrue("file doesn't exists after creation.", dao.exists(file
                .getId()));
        assertTrue("file doesn't exists after creation.", dao.exists(project
                .getId(), file.getName()));
        assertTrue("file name is not case insensitive.", dao.exists(project
                .getId(), file.getName()));
    }

    /**
     * project id is null
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
        dao.findByName(file.getProject().getId(), null);
    }

    /**
     * non-existing project id
     */
    @Test
    public void findByName3() {
        try {
            dao.findByName(NEW_PROJECT_ID, NAME);
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
            dao.findByName(file.getProject().getId(), NEW_NAME);
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
        assertEquals("files are not same.", file, dao.findByName(project
                .getId(), file.getName()));
        assertEquals("file name is not case insensitive.", file, dao
                .findByName(project.getId(), file.getName()));
    }

}
