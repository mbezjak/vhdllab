package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link UserFileDAOImpl}.
 *
 * @author Miro Bezjak
 */
public class UserFileDAOImplTest {

    private static final Caseless NAME = new Caseless("simple_file_name");
    private static final Caseless USER_ID = new Caseless("user.identifier");
    private static final String CONTENT = "<pref><value>schematic</value></pref>";
    private static final Caseless NEW_NAME = new Caseless("new_" + NAME);
    private static final Caseless NEW_USER_ID = new Caseless("new." + USER_ID);
    private static final String NEW_CONTENT = "library ieee;";

    private static UserFileDAO dao;
    private UserFile file;

    @BeforeClass
    public static void initTestCase() throws Exception {
        dao = new UserFileDAOImpl();
        EntityManagerUtil.createEntityManagerFactory();
        destroyFiles();
    }

    @Before
    public void initEachTest() throws Exception {
        initFiles();
        EntityManagerUtil.currentEntityManager();
    }

    private void initFiles() throws Exception {
        file = new UserFile(USER_ID, NAME, CONTENT);
    }

    @After
    public void destroyEachTest() throws Exception {
        EntityManagerUtil.closeEntityManager();
        destroyFiles();
    }

    private static void destroyFiles() throws Exception {
        /*
         * Create a new entity manager for destroying files to isolate any
         * errors in a test
         */
        EntityManagerUtil.currentEntityManager();
        for (UserFile f : dao.findByUser(USER_ID)) {
            dao.delete(f.getId());
        }
        for (UserFile f : dao.findByUser(NEW_USER_ID)) {
            dao.delete(f.getId());
        }
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * Save a file then load it and see it they are the same
     */
    @Test
    public void saveAndLoad() throws Exception {
        dao.save(file);
        UserFile loadedFile = dao.load(file.getId());
        assertEquals("file not equal after creating and loading it.", file,
                loadedFile);
        assertEquals("names are not same.", NAME, loadedFile.getName());
        assertEquals("ids are not same.", USER_ID, loadedFile.getUserId());
    }

    /**
     * File is null
     */
    @Test(expected = NullPointerException.class)
    public void save() throws Exception {
        dao.save(null);
    }

    /**
     * Once file is persisted an id is no longer null
     */
    @Test
    public void save2() throws Exception {
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
     * File name and user id are unique (i.e. form secondary key)
     */
    @Test
    public void save6() throws Exception {
        dao.save(file);
        UserFile newFile = new UserFile(USER_ID, NAME, NEW_CONTENT);
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
     * Save a file with same user id but different name
     */
    @Test
    public void save7() throws Exception {
        dao.save(file);
        UserFile newFile = new UserFile(USER_ID, NEW_NAME, NEW_CONTENT);
        dao.save(newFile);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
    }

    /**
     * Save a file with same name but different user id
     */
    @Test
    public void save8() throws Exception {
        dao.save(file);
        UserFile newFile = new UserFile(NEW_USER_ID, NAME, NEW_CONTENT);
        dao.save(newFile);
        assertTrue("new file not saved.", dao.exists(newFile.getId()));
        assertEquals("files are not same.", newFile, dao.load(newFile.getId()));
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
        assertFalse("file exists after it was deleted.", dao.exists(file
                .getUserId(), file.getName()));
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
     * user id is null
     */
    @Test(expected = NullPointerException.class)
    public void exists4() throws Exception {
        dao.exists(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = NullPointerException.class)
    public void exists5() throws Exception {
        dao.exists(USER_ID, null);
    }

    /**
     * non-existing name and user id
     */
    @Test
    public void exists6() throws Exception {
        assertFalse("file with unused user id exists.", dao.exists(NEW_USER_ID,
                NAME));
        assertFalse("file with unused name exists.", dao.exists(USER_ID,
                NEW_NAME));
    }

    /**
     * everything ok
     */
    @Test
    public void exists7() throws Exception {
        dao.save(file);
        assertTrue("file doesn't exists after creation.", dao.exists(file
                .getId()));
        assertTrue("file doesn't exists after creation.", dao.exists(file
                .getUserId(), file.getName()));
        assertTrue("user id and file name are not case insensitive.", dao
                .exists(file.getUserId(), file.getName()));
    }

    /**
     * user id is null
     */
    @Test(expected = NullPointerException.class)
    public void findByName() throws Exception {
        dao.findByName(null, NAME);
    }

    /**
     * name is null
     */
    @Test(expected = NullPointerException.class)
    public void findByName2() throws Exception {
        dao.findByName(USER_ID, null);
    }

    /**
     * non-existing user id
     */
    @Test
    public void findByName3() throws Exception {
        try {
            dao.findByName(NEW_USER_ID, NAME);
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
    public void findByName4() throws Exception {
        try {
            dao.findByName(USER_ID, NEW_NAME);
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
        assertEquals("files are not same.", file, dao.findByName(file
                .getUserId(), file.getName()));
        assertEquals("user id and file name are not case insensitive.", file,
                dao.findByName(file.getUserId(), file.getName()));
    }

    /**
     * user id is null
     */
    @Test(expected = NullPointerException.class)
    public void findByUser() throws Exception {
        dao.findByUser(null);
    }

    /**
     * non-existing user id
     */
    @Test()
    public void findByUser2() throws Exception {
        assertEquals("not an empty list.", Collections.emptyList(), dao
                .findByUser(NEW_USER_ID));
    }

    /**
     * everything ok. one file in collection
     */
    @Test
    public void findByUser3() throws Exception {
        dao.save(file);
        List<UserFile> files = new ArrayList<UserFile>(1);
        files.add(file);
        assertEquals("collections are not same.", files, dao.findByUser(file
                .getUserId()));
        assertEquals("user id is not case insensitive.", files, dao
                .findByUser(file.getUserId()));
    }

    /**
     * everything ok. two files in collection
     */
    @Test
    public void findByUser4() throws Exception {
        UserFile newFile = new UserFile(USER_ID, NEW_NAME, NEW_CONTENT);
        dao.save(file);
        dao.save(newFile);
        List<UserFile> files = new ArrayList<UserFile>(2);
        files.add(file);
        files.add(newFile);
        assertEquals("collections are not same.", files, dao.findByUser(file
                .getUserId()));
        assertEquals("user id is not case insensitive.", files, dao
                .findByUser(file.getUserId()));
    }

    /**
     * everything ok. two collections
     */
    @Test
    public void findByUser5() throws Exception {
        UserFile newFile = new UserFile(NEW_USER_ID, NEW_NAME, NEW_CONTENT);
        dao.save(file);
        dao.save(newFile);
        List<UserFile> collection1 = new ArrayList<UserFile>(1);
        List<UserFile> collection2 = new ArrayList<UserFile>(1);
        collection1.add(file);
        collection2.add(newFile);
        assertEquals("collections are not same.", collection1, dao
                .findByUser(file.getUserId()));
        assertEquals("collections are not same.", collection1, dao
                .findByUser(file.getUserId()));
        assertEquals("user id is not case insensitive.", collection2, dao
                .findByUser(newFile.getUserId()));
        assertEquals("user id is not case insensitive.", collection2, dao
                .findByUser(newFile.getUserId()));
    }

    /**
     * Test to see if hibernate second level cache is working
     */
    @Ignore("already tested")
    @Test
    public void cacheTest() throws Exception {
        // prepair test by storing 500 files in database
        System.out.print("Prepairing UserFile cache test...");
        EntityManagerUtil.currentEntityManager();
        for (int i = 0; i < 500; i++) {
            String name = "name" + (i + 1);
            UserFile f = new UserFile(USER_ID, new Caseless(name), NEW_CONTENT);
            dao.save(f);
        }
        EntityManagerUtil.closeEntityManager();
        System.out.println("done");
        /*
         * Register hibernate statistics service to see if second level cache is
         * working.
         */
        EntityManagerUtil.registerPersistenceJMX();

        /*
         * Actual test (not automated - requires user interaction). Note that
         * caching behavioral can't be determined just by looking at spent time.
         * One reason for this is because above test preparation indirectly
         * populates caches. On the other hand by simply viewing statistics of
         * persistence provider user can be sure that caches are working.
         * jconsole tool will help with that.
         */
        for (int i = 0; i < 5; i++) {
            EntityManagerUtil.currentEntityManager();
            long start = System.currentTimeMillis();
            List<UserFile> files = dao.findByUser(USER_ID);
            EntityManagerUtil.closeEntityManager();
            long end = System.currentTimeMillis();
            System.out.println("UserFileDAO.findByUser() - cache test: "
                    + (end - start) + "ms");
            assertNotNull("collection is null.", files);
        }
        /*
         * Pause so user can view statistics in jconsole
         */
        Thread.sleep(Long.MAX_VALUE);
    }

}
