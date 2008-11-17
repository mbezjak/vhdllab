package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_OPPOSITE_CASE;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import hr.fer.zemris.vhdllab.dao.UserFileDao;
import hr.fer.zemris.vhdllab.entities.UserFile;

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
 * Tests for {@link UserFileDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class UserFileDaoImplTest extends AbstractDaoSupport {

    @Autowired
    private UserFileDao dao;
    private UserFile file;

    @Before
    public void initEachTest() {
        file = new UserFile(USER_ID, NAME, DATA);
    }

    /**
     * File name and user id are unique (i.e. form secondary key).
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveDuplicate() {
        setupUserFile(file);
        UserFile newFile = new UserFile(USER_ID, NAME, DATA_2);
        dao.save(newFile);
    }

    /**
     * Save a file with same user id but different name.
     */
    @Test
    public void saveDifferentName() {
        setupUserFile(file);
        UserFile newFile = new UserFile(USER_ID, NAME_2, DATA_2);
        dao.save(newFile);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Name is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveNameCaseInsensitive() {
        setupUserFile(file);
        UserFile newFile = new UserFile(USER_ID, NAME_OPPOSITE_CASE, DATA_2);
        dao.save(newFile);
    }

    /**
     * Save a file with same name but different user id.
     */
    @Test
    public void saveDifferentUserId() {
        setupUserFile(file);
        UserFile newFile = new UserFile(USER_ID_2, NAME, DATA_2);
        dao.save(newFile);
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * User id is case insensitive.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void saveUserIdCaseInsensitive() {
        setupUserFile(file);
        UserFile newFile = new UserFile(USER_ID_OPPOSITE_CASE, NAME, DATA_2);
        dao.save(newFile);
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
        UserFile newFile = dao.findByName(USER_ID_2, NAME);
        assertNull("file already exists.", newFile);
    }

    /**
     * non-existing name
     */
    @Test
    public void findByNameNonExistingName() {
        UserFile newFile = dao.findByName(USER_ID, NAME_2);
        assertNull("file already exists.", newFile);
    }

    /**
     * everything ok
     */
    @Test
    public void findByName() {
        setupUserFile(file);
        assertEquals("files are not same.", file, dao.findByName(file
                .getUserId(), file.getName()));
        assertEquals("user id and file name are not case insensitive.", file,
                dao.findByName(USER_ID_OPPOSITE_CASE, NAME_OPPOSITE_CASE));
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
        assertEquals("not an empty list.", Collections.emptyList(), dao
                .findByUser(USER_ID_2));
    }

    /**
     * everything ok. one file in collection
     */
    @Test
    public void findByUser() {
        setupUserFile(file);
        List<UserFile> files = new ArrayList<UserFile>(1);
        files.add(file);
        assertEquals("collections are not same.", files, dao.findByUser(file
                .getUserId()));
        assertEquals("user id is not case insensitive.", files, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
    }

    /**
     * everything ok. two files in collection
     */
    @Test
    public void findByUser2() {
        UserFile newFile = new UserFile(USER_ID, NAME_2, DATA_2);
        setupUserFile(file);
        setupUserFile(newFile);
        List<UserFile> files = new ArrayList<UserFile>(2);
        files.add(file);
        files.add(newFile);
        assertEquals("collections are not same.", files, dao.findByUser(file
                .getUserId()));
        assertEquals("user id is not case insensitive.", files, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
    }

    /**
     * everything ok. two collections
     */
    @Test
    public void findByUser3() {
        UserFile newFile = new UserFile(USER_ID_2, NAME_2, DATA_2);
        setupUserFile(file);
        setupUserFile(newFile);
        List<UserFile> collection1 = new ArrayList<UserFile>(1);
        List<UserFile> collection2 = new ArrayList<UserFile>(1);
        collection1.add(file);
        collection2.add(newFile);
        assertEquals("collections are not same.", collection1, dao
                .findByUser(file.getUserId()));
        assertEquals("collections are not same.", collection1, dao
                .findByUser(USER_ID_OPPOSITE_CASE));
        assertEquals("user id is not case insensitive.", collection2, dao
                .findByUser(newFile.getUserId()));
        assertEquals("user id is not case insensitive.", collection2, dao
                .findByUser(newFile.getUserId().toUpperCase()));
    }

    private void setupUserFile(final UserFile file) {
        String query = createQuery("user_files",
                "id, version, name, user_id, data", "null, 0, ?, ?, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, file.getName().toString());
                ps.setString(2, file.getUserId().toString());
                ps.setString(3, file.getData());
                return ps.execute();
            }
        });
    }

}
