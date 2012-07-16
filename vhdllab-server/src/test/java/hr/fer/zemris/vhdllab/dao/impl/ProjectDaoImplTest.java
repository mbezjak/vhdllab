/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private void setupProject(final Project project) {
        String query = createInsertStatement("projects",
                "id, version, name, user_id", "null, 0, ?, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, project.getName());
                ps.setString(2, project.getUserId());
                return ps.execute();
            }
        });
    }

}
