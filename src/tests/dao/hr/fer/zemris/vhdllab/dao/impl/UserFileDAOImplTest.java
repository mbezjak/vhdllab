package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.dao.impl.StringGenerationUtil.generateJunkString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
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

	private static final String NAME = "simple.file.name";
	private static final String USER_ID = "user.identifier";
	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);
	private static final String UNUSED_NAME = "unused.name";
	private static final String UNUSED_USER_ID = "unused.user.identifier";
	private static final int MAX_NAME_LENGTH = 255;
	private static final int MAX_USER_ID_LENGTH = MAX_NAME_LENGTH;
	private static final int MAX_CONTENT_LENGTH = 65535;

	private static UserFileDAO dao;
	private UserFile file;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		dao = new UserFileDAOImpl();
		EntityManagerUtil.createEntityManagerFactory();
		destroyFiles();
	}

	@Before
	public void initEachTest() throws DAOException {
		initFiles();
		EntityManagerUtil.currentEntityManager();
	}

	private void initFiles() throws DAOException {
		file = new UserFile();
		file.setUserId(USER_ID);
		file.setName(NAME);
		file.setContent("<pref><value>schematic</value></pref>");
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
		for (UserFile f : dao.findByUser(USER_ID)) {
			dao.delete(f.getId());
		}
		for (UserFile f : dao.findByUser(UNUSED_USER_ID)) {
			dao.delete(f.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * save a file then load it and see it they are the same
	 */
	@Test
	public void saveAndLoad() throws DAOException {
		dao.save(file);
		UserFile loadedFile = dao.load(file.getId());
		assertEquals(file, loadedFile);
	}

	/**
	 * save a file then delete it
	 */
	@Test
	public void delete() throws DAOException {
		dao.save(file);
		dao.delete(file.getId());
		assertEquals(false, dao.exists(file.getId()));
		assertEquals(false, dao.exists(file.getUserId(), file.getName()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists(null, NAME);
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists2() throws DAOException {
		dao.exists(USER_ID, null);
	}

	/**
	 * non-existing name and user id
	 */
	@Test
	public void exists3() throws DAOException {
		assertEquals(false, dao.exists(UNUSED_USER_ID, NAME));
		assertEquals(false, dao.exists(USER_ID, UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists4() throws DAOException {
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(true, dao.exists(file.getUserId(), file.getName()));
		assertEquals(true, dao.exists(file.getUserId().toUpperCase(), file
				.getName().toUpperCase()));
	}

	/**
	 * Once file is persisted an ID is no longer null
	 */
	@Test()
	public void save() throws DAOException {
		assertEquals(null, file.getId());
		dao.save(file);
		assertNotSame(null, file.getId());
	}

	/**
	 * ID can't be a part of insert statement
	 */
	@Test(expected = DAOException.class)
	public void save2() throws DAOException {
		file.setId(UNUSED_ID);
		dao.save(file);
	}

	/**
	 * ID can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save3() throws DAOException {
		dao.save(file);
		file.setId(UNUSED_ID);
		dao.save(file);
	}

	/**
	 * user id is null
	 */
	@Test(expected = DAOException.class)
	public void save4() throws DAOException {
		file.setUserId(null);
		dao.save(file);
	}

	/**
	 * user id is of max length
	 */
	@Test()
	public void save5() throws DAOException {
		file.setUserId(generateJunkString(MAX_USER_ID_LENGTH));
		dao.save(file);
		// delete file afterwards
		dao.delete(file.getId());
	}

	/**
	 * user id is too long
	 */
	@Test(expected = DAOException.class)
	public void save6() throws DAOException {
		file.setUserId(generateJunkString(MAX_USER_ID_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * name is null
	 */
	@Test(expected = DAOException.class)
	public void save7() throws DAOException {
		file.setName(null);
		dao.save(file);
	}

	/**
	 * user id and name are unique
	 */
	@Test(expected = DAOException.class)
	public void save8() throws DAOException {
		dao.save(file);
		UserFile clone = new UserFile(file);
		clone.setId(null);
		clone.setContent("abc");
		dao.save(clone);
	}

	/**
	 * name is of max length
	 */
	@Test()
	public void save9() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH));
		dao.save(file);
	}

	/**
	 * name is too long
	 */
	@Test(expected = DAOException.class)
	public void save10() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * content is null
	 */
	@Test
	public void save11() throws DAOException {
		file.setContent(null);
		dao.save(file);
	}

	/**
	 * content is of max length
	 */
	@Test()
	public void save12() throws DAOException {
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH));
		dao.save(file);
	}

	/**
	 * content is too long
	 */
	@Test(expected = DAOException.class)
	public void save13() throws DAOException {
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * save a file then update it
	 */
	@Test
	public void save14() throws DAOException {
		dao.save(file);
		file.setUserId(UNUSED_USER_ID);
		file.setName(UNUSED_NAME);
		file.setContent("abc");
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(file, dao.load(file.getId()));
	}

	/**
	 * save a file with same user id but different name
	 */
	@Test
	public void save15() throws DAOException {
		UserFile newFile = new UserFile();
		newFile.setUserId(USER_ID);
		newFile.setName(UNUSED_NAME);
		newFile.setContent("abc");
		dao.save(file);
		dao.save(newFile);
		assertEquals(true, dao.exists(newFile.getId()));
		assertEquals(newFile, dao.load(newFile.getId()));
	}

	/**
	 * save a file with same name but different user id
	 */
	@Test
	public void save16() throws DAOException {
		UserFile newFile = new UserFile();
		newFile.setUserId(UNUSED_USER_ID);
		newFile.setName(NAME);
		newFile.setContent("abc");
		dao.save(file);
		dao.save(newFile);
		assertEquals(true, dao.exists(newFile.getId()));
		assertEquals(newFile, dao.load(newFile.getId()));
	}

	/**
	 * user id is null
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
		dao.findByName(USER_ID, null);
	}

	/**
	 * non-existing user id
	 */
	@Test(expected = DAOException.class)
	public void findByName3() throws DAOException {
		dao.findByName(UNUSED_USER_ID, NAME);
	}

	/**
	 * non-existing name
	 */
	@Test(expected = DAOException.class)
	public void findByName4() throws DAOException {
		dao.findByName(USER_ID, UNUSED_NAME);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws DAOException {
		dao.save(file);
		assertEquals(file, dao.findByName(file.getUserId(), file.getName()));
		assertEquals(file, dao.findByName(file.getUserId().toUpperCase(), file
				.getName().toUpperCase()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void findByUser() throws DAOException {
		dao.findByUser(null);
	}

	/**
	 * non-existing user id
	 */
	@Test()
	public void findByUser2() throws DAOException {
		assertEquals(Collections.emptyList(), dao.findByUser(UNUSED_USER_ID));
	}

	/**
	 * everything ok. one file in collection
	 */
	@Test
	public void findByUser3() throws DAOException {
		dao.save(file);
		List<UserFile> files = new ArrayList<UserFile>(1);
		files.add(file);
		assertEquals(files, dao.findByUser(file.getUserId()));
		assertEquals(files, dao.findByUser(file.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two files in collection
	 */
	@Test
	public void findByUser4() throws DAOException {
		UserFile file2 = new UserFile();
		file2.setUserId(USER_ID);
		file2.setName(UNUSED_NAME);
		file2.setContent("abc");
		dao.save(file);
		dao.save(file2);
		List<UserFile> files = new ArrayList<UserFile>(2);
		files.add(file);
		files.add(file2);
		assertEquals(files, dao.findByUser(file.getUserId()));
		assertEquals(files, dao.findByUser(file.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two collections
	 */
	@Test
	public void findByUser5() throws DAOException {
		UserFile file2 = new UserFile();
		file2.setUserId(UNUSED_USER_ID);
		file2.setName(UNUSED_NAME);
		file2.setContent("abc");
		dao.save(file);
		dao.save(file2);
		List<UserFile> collection1 = new ArrayList<UserFile>(1);
		List<UserFile> collection2 = new ArrayList<UserFile>(1);
		collection1.add(file);
		collection2.add(file2);
		assertEquals(collection1, dao.findByUser(file.getUserId()));
		assertEquals(collection1, dao
				.findByUser(file.getUserId().toUpperCase()));
		assertEquals(collection2, dao.findByUser(file2.getUserId()));
		assertEquals(collection2, dao.findByUser(file2.getUserId()
				.toUpperCase()));
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
			UserFile f = new UserFile();
			f.setUserId(USER_ID);
			f.setName("name" + (i + 1));
			f.setContent("abcdef");
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
			System.out.println("UserFile.findByUser() - cache test: "
					+ (end - start) + "ms");
			assertNotSame(null, files);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

}
