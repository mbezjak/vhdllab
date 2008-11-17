package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;

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
 * Tests for {@link ProjectDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectDaoImplTest extends AbstractDaoSupport {

    @Autowired
    private FileDao fileDAO;
    @Autowired
    private ProjectDao dao;
    private Project project;
    private File file;

    @Before
    public void initEachTest() {
        project = new Project(USER_ID, NAME);
        file = new File(TYPE, NAME, DATA);
        project.addFile(file);
    }

    /**
     * Cascade to saving files when creating a project.
     */
    @Test
    public void saveCascade() {
        dao.save(project);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
    }

    /**
     * Project name and user id are unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        setupProject(project);
        Project newProject = new Project(USER_ID, NAME);
        dao.save(newProject);
    }

    /**
     * Save a project with same user id but different name.
     */
    @Test
    public void saveDifferentName() {
        setupProject(project);
        Project newProject = new Project(USER_ID, NAME_2);
        dao.save(newProject);
        assertEquals("projects are not same.", newProject, dao.load(newProject
                .getId()));
    }

    /**
     * Name is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNameCaseInsensitive() {
        setupProject(project);
        Project newProject = new Project(USER_ID, NAME_OPPOSITE_CASE);
        dao.save(newProject);
    }

    /**
     * Save a project with same name but different user id.
     */
    @Test
    public void saveDifferentUserId() {
        setupProject(project);
        Project newProject = new Project(USER_ID_2, NAME);
        dao.save(newProject);
        assertEquals("projects are not same.", newProject, dao.load(newProject
                .getId()));
    }

    /**
     * User id is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveUserIdCaseInsensitive() {
        setupProject(project);
        Project newProject = new Project(USER_ID_OPPOSITE_CASE, NAME);
        dao.save(newProject);
    }

    /**
     * add a file after a project is saved.
     */
    @Test
    public void saveAddFile() {
        dao.save(project);
        File newFile = new File(TYPE_2, NAME_2, DATA_2);
        project.addFile(newFile);
        dao.save(project);

        Project loadedProject = dao.load(project.getId());
        assertNotNull("file not saved.", fileDAO.load(newFile.getId()));
        assertNotNull("file not saved.", fileDAO.findByName(loadedProject
                .getId(), newFile.getName()));
        assertEquals("projects not equal.", project, loadedProject);
        assertTrue("project doesn't contain a new file.", loadedProject
                .getFiles().contains(newFile));
    }

    /**
     * remove a file from a project then save project.
     */
    @Test
    public void saveRemoveFile() {
        dao.save(project);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
        project.removeFile(file);
        assertNull("file not removed from collection.", file.getProject());
        assertFalse("file still in collection.", project.getFiles().contains(
                file));
        dao.save(project);
        assertNull("file still exists.", fileDAO.load(file.getId()));
        assertFalse("project still contains a file.", project.getFiles()
                .contains(file));
    }

    /**
     * user id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullUserId() {
        dao.findByName(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullName() {
        dao.findByName(USER_ID, null);
    }

    /**
     * non-existing user id
     */
    @Test
    public void findByNameNonExistingUserId() {
        Project newProject = dao.findByName(USER_ID_2, NAME);
        assertNull("project already exists.", newProject);
    }

    /**
     * non-existing name
     */
    @Test
    public void findByNameNonExistingName() {
        Project newProject = dao.findByName(USER_ID, NAME_2);
        assertNull("project already exists.", newProject);
    }

    /**
     * everything ok
     */
    @Test
    public void findByName() {
        setupProject(project);
        assertEquals("project not found.", project, dao.findByName(project
                .getUserId(), project.getName()));
        assertEquals("project name and user id are not case insensitive.",
                project, dao.findByName(USER_ID_OPPOSITE_CASE,
                        NAME_OPPOSITE_CASE));
    }

    /**
     * user id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByUserNullUserId() {
        dao.findByUser(null);
    }

    /**
     * non-existing user id
     */
    @Test
    public void findByUserNonExistingUserId() {
        assertEquals(Collections.emptyList(), dao.findByUser(USER_ID_2));
    }

    /**
     * everything ok. one project in collection
     */
    @Test
    public void findByUser() {
        setupProject(project);
        List<Project> projects = new ArrayList<Project>(1);
        projects.add(project);
        assertEquals("projects not equal.", projects, dao.findByUser(project
                .getUserId()));
        assertEquals("user id is not case insensitive.", projects, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
    }

    /**
     * everything ok. two projects in collection
     */
    @Test
    public void findByUser2() {
        Project project2 = new Project(USER_ID, NAME_2);
        setupProject(project);
        setupProject(project2);
        List<Project> projects = new ArrayList<Project>(2);
        projects.add(project);
        projects.add(project2);
        assertEquals("collections not equal.", projects, dao.findByUser(project
                .getUserId()));
        assertEquals("user id is not case insensitive.", projects, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
    }

    /**
     * everything ok. two collections
     */
    @Test
    public void findByUser3() {
        Project project2 = new Project(USER_ID_2, NAME_2);
        setupProject(project);
        setupProject(project2);
        List<Project> collection1 = new ArrayList<Project>(1);
        List<Project> collection2 = new ArrayList<Project>(1);
        collection1.add(project);
        collection2.add(project2);
        assertEquals("collections not equal.", collection1, dao
                .findByUser(project.getUserId()));
        assertEquals("user id is not case insensitive.", collection1, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
        assertEquals("collections not equal.", collection2, dao
                .findByUser(project2.getUserId()));
        assertEquals("user id is not case insensitive.", collection2, dao
                .findByUser(project2.getUserId().toUpperCase()));
    }

    private void setupProject(final Project project) {
        String query = createQuery("projects", "id, version, name, user_id",
                "null, 0, ?, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, project.getName().toString());
                ps.setString(2, project.getUserId().toString());
                return ps.execute();
            }
        });
    }

}
