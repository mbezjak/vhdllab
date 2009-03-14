package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.validator.InvalidStateException;
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

    private static final String USER_ID = "userId";
    private static final String USER_ID_2 = "anotherUserId";
    private static final String USER_ID_OPPOSITE_CASE = USER_ID.toUpperCase();
    private static final String NAME = "entity_name";
    private static final String NAME_2 = "another_entity_name";
    private static final String NAME_OPPOSITE_CASE = NAME.toUpperCase();

    @Autowired
    private FileDao fileDAO;
    @Autowired
    private ProjectDao dao;
    private Project project;
    private File file;

    @Before
    public void initEachTest() {
        project = new Project(USER_ID, NAME);
        file = new File("file_name", FileType.SOURCE, "file data");
        project.addFile(file);
    }

    @Test(expected = InvalidStateException.class)
    public void illegalName() {
        project.setName("_illegal_name");
        dao.persist(project);
    }

    /**
     * Cascade to saving files when creating a project.
     */
    @Test
    public void saveCascade() {
        dao.persist(project);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
    }

    /**
     * Project name and user id are unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        setupProject(project);
        Project newProject = new Project(USER_ID, NAME);
        dao.persist(newProject);
    }

    /**
     * Save a project with same user id but different name.
     */
    @Test
    public void saveDifferentName() {
        setupProject(project);
        Project newProject = new Project(USER_ID, NAME_2);
        dao.persist(newProject);
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
        dao.persist(newProject);
    }

    /**
     * Save a project with same name but different user id.
     */
    @Test
    public void saveDifferentUserId() {
        setupProject(project);
        Project newProject = new Project(USER_ID_2, NAME);
        dao.persist(newProject);
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
        dao.persist(newProject);
    }

    /**
     * add a file after a project is saved.
     */
    @Test
    public void saveAddFile() {
        dao.persist(project);
        File newFile = new File(NAME_2, FileType.AUTOMATON, "another file data");
        project.addFile(newFile);
        dao.persist(project);

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
        dao.persist(project);
        assertNotNull("file not saved.", fileDAO.load(file.getId()));
        project.removeFile(file);
        assertNull("file not removed from collection.", file.getProject());
        assertFalse("file still in collection.", project.getFiles().contains(
                file));
        dao.persist(project);
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

    /**
     * Preferences project doesn't exist.
     */
    @Test
    public void getPreferencesProject() {
        Query query = entityManager
                .createQuery("select p from Project p where p.type = ?1");
        query.setParameter(1, ProjectType.PREFERENCES);
        assertTrue(query.getResultList().isEmpty());

        Project preferencesProject = dao.getPreferencesProject(USER_ID);
        assertNotNull(preferencesProject);
        assertFalse(preferencesProject.isNew());
        assertEquals(USER_ID, preferencesProject.getUserId());
        assertEquals(ProjectType.PREFERENCES, preferencesProject.getType());

        assertFalse(query.getResultList().isEmpty());
        Project newlyLoadedPreferencesProject = dao
                .getPreferencesProject(USER_ID);
        assertEquals(preferencesProject, newlyLoadedPreferencesProject);
        assertEquals(preferencesProject.getId(), newlyLoadedPreferencesProject
                .getId());
    }

    private void setupProject(final Project project) {
        String query = createInsertStatement("projects",
                "id, version, name, user_id, type", "null, 0, ?, ?, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, project.getName());
                ps.setString(2, project.getUserId());
                ps.setString(3, project.getType().toString());
                return ps.execute();
            }
        });
    }

}
