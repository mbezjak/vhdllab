package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;

import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link FileDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileDaoImplTest extends AbstractDaoSupport {

    private static final String USER_ID = "userId";
    private static final String NAME = "entity_name";
    private static final String NAME_2 = "another_entity_name";
    private static final String NAME_OPPOSITE_CASE = NAME.toUpperCase();
    private static final FileType TYPE = FileType.SOURCE;
    private static final String DATA = "file data";

    @Autowired
    private FileDao dao;
    @Autowired
    private ProjectDao projectDAO;
    private Project project;
    private File file;

    @Before
    public void initEachTest() {
        project = new Project(USER_ID, NAME);
        projectDAO.persist(project);

        file = new File(NAME, TYPE, DATA);
        project.addFile(file);
    }

    @Test(expected = InvalidStateException.class)
    public void illegalName() {
        file.setName("_illegal_name");
        dao.persist(file);
    }

    /**
     * save a file then load it and see it they are the same.
     */
    @Test
    public void saveAndLoad() {
        dao.persist(file);
        File loadedFile = dao.load(file.getId());
        assertEquals("file not equal after creating and loading it.", file,
                loadedFile);
        assertEquals("projects are not same.", project, loadedFile.getProject());
    }

    /**
     * non-existing project (can't cascade to persist a project).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNonExistingProject() {
        Project newProject = new Project(USER_ID, NAME_2);
        File newFile = new File(file);
        newProject.addFile(newFile);
        dao.persist(newFile);
    }

    /**
     * If project is saved then file can be persisted.
     */
    @Test
    public void saveExistingProject() {
        Project newProject = new Project(USER_ID, NAME_2);
        projectDAO.persist(newProject);
        File newFile = new File(file.getName(), file.getType(), file.getData());
        newProject.addFile(newFile);
        dao.persist(newFile);
        assertEquals("files not same.", newFile, dao.load(newFile.getId()));
        assertEquals("files not same.", newFile, dao.findByName(newProject
                .getId(), newFile.getName()));
        Project loadedProject = projectDAO.load(newProject.getId());
        assertTrue("collection isn't updated.", loadedProject.getFiles()
                .contains(newFile));
    }

    /**
     * File name and project id are unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        dao.persist(file);
        File newFile = new File(file.getName(), TYPE, DATA);
        project.addFile(newFile);
        dao.persist(newFile);
    }

    /**
     * Save a file with same project but different name.
     */
    @Test
    public void saveDifferentName() {
        dao.persist(file);
        File newFile = new File(NAME_2, TYPE, DATA);
        project.addFile(newFile);
        dao.persist(newFile);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Name is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNameCaseInsensitive() {
        dao.persist(file);
        File newFile = new File(NAME_OPPOSITE_CASE, TYPE, DATA);
        project.addFile(newFile);
        dao.persist(newFile);
    }

    /**
     * Save a file with same name but different project.
     */
    @Test
    public void saveDifferentProject() {
        dao.persist(file);
        Project newProject = new Project(USER_ID, NAME_2);
        projectDAO.persist(newProject);
        File newFile = new File(NAME, TYPE, DATA);
        newProject.addFile(newFile);
        dao.persist(file);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Save a project and a file in same session then delete a file.
     */
    @Test
    public void saveAndDeleteInSameSession() {
        Project newProject = new Project(project.getUserId(), NAME_2);
        projectDAO.persist(newProject);
        File newFile = new File(file.getName(), file.getType(), file.getData());
        newProject.addFile(newFile);
        dao.persist(newFile);
        assertEquals("file not saved.", newFile, dao.load(newFile.getId()));
        dao.delete(newFile);
        assertNull("file exists after it was deleted.", dao.load(newFile
                .getId()));
        assertNull("file exists after it was deleted.", dao.findByName(
                newProject.getId(), newFile.getName()));
    }

    /**
     * project id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullProjectId() {
        dao.findByName(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByNameNullName() {
        dao.findByName(file.getProject().getId(), null);
    }

    /**
     * non-existing project id
     */
    @Test
    public void findByNameNonExistingProjectId() {
        assertNull("file already exists.", dao.findByName(Integer.MAX_VALUE,
                NAME));
    }

    /**
     * non-existing name
     */
    @Test
    public void findByNameNonExistingName() {
        assertNull("file already exists.", dao.findByName(file.getProject()
                .getId(), NAME_2));
    }

    /**
     * everything ok
     */
    @Test
    public void findByName() {
        dao.persist(file);
        assertEquals("files are not same.", file, dao.findByName(project
                .getId(), file.getName()));
        assertEquals("file name is not case insensitive.", file, dao
                .findByName(project.getId(), NAME_OPPOSITE_CASE));
    }

}
