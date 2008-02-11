package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.dao.impl.StringGenerationUtil.generateJunkString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.server.FileTypes;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

	private static final Class<UserFile> clazz = UserFile.class;
	private static final String NAME = "simple.file.name";
	private static final String USER_ID = "user.identifier";
	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);
	private static final String UNUSED_NAME = "unused.name";
	private static final String UNUSED_USER_ID = "unused.user.identifier";

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
		file = new UserFile();
		file.setUserId(USER_ID);
		file.setName(NAME);
		file.setContent("<pref><value>schematic</value></pref>");
		file.setType(FileTypes.VHDL_SCHEMA);
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
		for (UserFile f : dao.findByUser(UNUSED_USER_ID)) {
			dao.delete(f.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * Create a file then load it and see it they are the same
	 */
	@Test
	public void createAndLoad() throws Exception {
		dao.create(file);
		UserFile loadedFile = dao.load(file.getId());
		assertEquals("File not equal after creating and loading it.", file,
				loadedFile);
		assertEquals("Files names are not same.", NAME, loadedFile.getName());
		assertEquals("User ids are not same.", USER_ID, loadedFile.getUserId());
	}

	/**
	 * File is null
	 */
	@Test(expected = NullPointerException.class)
	public void create() throws Exception {
		dao.create(null);
	}

	/**
	 * Once file is persisted an id is no longer null
	 */
	@Test
	public void create2() throws Exception {
		assertNull("File has id set.", file.getId());
		dao.create(file);
		assertNotNull("File id wasn't set after creation.", file.getId());
	}

	/**
	 * id can't be a part of insert statement
	 */
	@Test(expected = DAOException.class)
	public void create3() throws Exception {
		file.setId(UNUSED_ID);
		dao.create(file);
	}

	/**
	 * File name is null
	 */
	@Test(expected = DAOException.class)
	public void create4() throws Exception {
		file.setName(null);
		dao.create(file);
	}

	/**
	 * File name is too long
	 */
	@Test
	public void create6() throws Exception {
		int length = DAOUtil.columnLengthFor(clazz, "name");
		file.setName(generateJunkString(length + 1));
		try {
			dao.create(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_NAME_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File type is null
	 */
	@Test(expected = DAOException.class)
	public void create7() throws Exception {
		file.setType(null);
		dao.create(file);
	}

	/**
	 * File type is too long
	 */
	@Test(expected = DAOException.class)
	public void create8() throws Exception {
		int length = DAOUtil.columnLengthFor(clazz, "type");
		file.setType(generateJunkString(length + 1));
		dao.create(file);
	}

	/**
	 * File type can't be any string. Must be only one of registered file types.
	 */
	@Test
	public void create9() throws Exception {
		file.setType("invalid.file.type");
		try {
			dao.create(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_INVALID_FILE_TYPE) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File content is null
	 */
	@Test(expected = DAOException.class)
	public void create10() throws Exception {
		file.setContent(null);
		dao.create(file);
	}

	/**
	 * File content is too long
	 */
	@Test
	public void create11() throws Exception {
		int length = DAOUtil.columnLengthFor(clazz, "content");
		file.setContent(generateJunkString(length + 1));
		try {
			dao.create(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_CONTENT_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Once file is persisted created date is no longer null
	 */
	@Test
	public void create12() throws Exception {
		assertNull("File has created date set.", file.getCreated());
		dao.create(file);
		assertNotNull("Created date wasn't set after creation.", file.getId());
	}

	/**
	 * Created date can't be set before creation
	 */
	@Test(expected = DAOException.class)
	public void create13() throws Exception {
		file.setCreated(new Date());
		dao.create(file);
	}

	/**
	 * User id is null
	 */
	@Test(expected = DAOException.class)
	public void create14() throws Exception {
		file.setUserId(null);
		dao.create(file);
	}

	/**
	 * User id is too long
	 */
	@Test
	public void create15() throws Exception {
		int length = DAOUtil.columnLengthFor(clazz, "userId");
		file.setUserId(generateJunkString(length + 1));
		try {
			dao.create(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_USER_ID_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File name and user id are unique (i.e. form secondary key)
	 */
	@Test
	public void create16() throws Exception {
		dao.create(file);
		UserFile newFile = new UserFile();
		newFile.setName(NAME);
		newFile.setUserId(USER_ID);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setContent("library ieee;");
		try {
			dao.create(newFile);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_ALREADY_EXISTS) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Create a file with same user id but different name
	 */
	@Test
	public void create17() throws Exception {
		dao.create(file);
		UserFile newFile = new UserFile();
		newFile.setName(UNUSED_NAME);
		newFile.setUserId(USER_ID);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setContent("library ieee;");
		dao.create(newFile);
		assertTrue("New file not save.", dao.exists(newFile.getId()));
		assertEquals("Files are not same.", newFile, dao.load(newFile.getId()));
	}

	/**
	 * Create a file with same name but different user id
	 */
	@Test
	public void create18() throws Exception {
		dao.create(file);
		UserFile newFile = new UserFile();
		newFile.setName(NAME);
		newFile.setUserId(UNUSED_USER_ID);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setContent("library ieee;");
		dao.create(newFile);
		assertTrue("New file not save.", dao.exists(newFile.getId()));
		assertEquals("Files are not same.", newFile, dao.load(newFile.getId()));
	}

	/**
	 * File is null
	 */
	@Test(expected = NullPointerException.class)
	public void save() throws Exception {
		dao.save(null);
	}

	/**
	 * id must be set when saving a file.
	 */
	@Test(expected = DAOException.class)
	public void save2() throws Exception {
		file.setId(null);
		dao.save(file);
	}

	/**
	 * id can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save3() throws Exception {
		dao.create(file);
		file.setId(UNUSED_ID);
		dao.save(file);
	}

	/**
	 * File name must be set when saving a file.
	 */
	@Test(expected = DAOException.class)
	public void save4() throws Exception {
		dao.create(file);
		file.setName(null);
		dao.save(file);
	}

	/**
	 * File name can be a part of update statement
	 */
	@Test
	public void save5() throws Exception {
		dao.create(file);
		file.setName(UNUSED_NAME);
		dao.save(file);
		assertEquals("Files not same after name was updated.", file, dao
				.load(file.getId()));
	}

	/**
	 * File name is too long
	 */
	@Test
	public void save6() throws Exception {
		dao.create(file);
		int length = DAOUtil.columnLengthFor(clazz, "name");
		file.setName(generateJunkString(length + 1));
		try {
			dao.save(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_NAME_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File type is null
	 */
	@Test(expected = DAOException.class)
	public void save7() throws Exception {
		dao.create(file);
		file.setType(null);
		dao.save(file);
	}

	/**
	 * File type can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save8() throws Exception {
		dao.create(file);
		file.setType(FileTypes.VHDL_SOURCE);
		dao.save(file);
	}

	/**
	 * File type is too long
	 */
	@Test(expected = DAOException.class)
	public void save9() throws Exception {
		dao.create(file);
		int length = DAOUtil.columnLengthFor(clazz, "type");
		file.setType(generateJunkString(length + 1));
		dao.save(file);
	}

	/**
	 * File type can't be any string. Must be only one of registered file types.
	 */
	@Test
	public void save10() throws Exception {
		dao.create(file);
		file.setType("invalid.file.type");
		try {
			dao.save(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_INVALID_FILE_TYPE) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File content is null
	 */
	@Test(expected = DAOException.class)
	public void save11() throws Exception {
		dao.create(file);
		file.setContent(null);
		dao.save(file);
	}

	/**
	 * File content can be a part of update statement
	 */
	@Test
	public void save12() throws Exception {
		dao.create(file);
		file.setContent("a new content");
		dao.save(file);
		assertEquals("Files not same after content was updated.", file, dao
				.load(file.getId()));
	}

	/**
	 * File content is too long
	 */
	@Test
	public void save13() throws Exception {
		dao.create(file);
		int length = DAOUtil.columnLengthFor(clazz, "content");
		file.setContent(generateJunkString(length + 1));
		try {
			dao.save(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_CONTENT_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Created date is null
	 */
	@Test(expected = DAOException.class)
	public void save14() throws Exception {
		dao.create(file);
		file.setCreated(null);
		dao.save(file);
	}

	/**
	 * Created date can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save15() throws Exception {
		dao.create(file);
		file.setCreated(new Date());
		dao.save(file);
	}

	/**
	 * User id is null
	 */
	@Test(expected = DAOException.class)
	public void save16() throws Exception {
		dao.create(file);
		file.setUserId(null);
		dao.save(file);
	}

	/**
	 * User id is too long
	 */
	@Test
	public void save17() throws Exception {
		dao.create(file);
		int length = DAOUtil.columnLengthFor(clazz, "userId");
		file.setUserId(generateJunkString(length + 1));
		try {
			dao.save(file);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_USER_ID_TOO_LONG) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * User id can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save18() throws Exception {
		dao.create(file);
		file.setUserId(UNUSED_USER_ID);
		dao.save(file);
	}

	/**
	 * File name and user id are unique (i.e. form secondary key)
	 */
	@Test
	public void save19() throws Exception {
		dao.create(file);
		UserFile newFile = new UserFile();
		newFile.setName(UNUSED_NAME);
		newFile.setUserId(USER_ID);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setContent("library ieee;");
		dao.create(newFile);
		newFile.setName(NAME);
		try {
			dao.save(newFile);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Save a file with same user id but different name
	 */
	@Test
	public void save20() throws Exception {
		dao.create(file);
		UserFile newFile = new UserFile();
		newFile.setName(UNUSED_NAME);
		newFile.setUserId(USER_ID);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setContent("library ieee;");
		dao.create(newFile);
		newFile.setName("different.file.name");
		dao.save(newFile);
		assertTrue("New file not save.", dao.exists(newFile.getId()));
		assertEquals("Files are not same.", newFile, dao.load(newFile.getId()));
	}

	/**
	 * Create a file then delete it
	 */
	@Test
	public void delete() throws Exception {
		dao.create(file);
		dao.delete(file.getId());
		assertFalse("File exists after it was deleted.", dao.exists(file
				.getId()));
		assertFalse("File exists after it was deleted.", dao.exists(file
				.getUserId(), file.getName()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws Exception {
		dao.exists(null, NAME);
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists2() throws Exception {
		dao.exists(USER_ID, null);
	}

	/**
	 * non-existing name and user id
	 */
	@Test
	public void exists3() throws Exception {
		assertFalse("File with unused user id exists.", dao.exists(
				UNUSED_USER_ID, NAME));
		assertFalse("File with unused name exists.", dao.exists(USER_ID,
				UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists4() throws Exception {
		dao.create(file);
		assertTrue("File doesn't exists after creation", dao.exists(file
				.getId()));
		assertTrue("File doesn't exists after creation", dao.exists(file
				.getUserId(), file.getName()));
		assertTrue("User id and file name are not case insensitive", dao
				.exists(file.getUserId().toUpperCase(), file.getName()
						.toUpperCase()));
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
	@Test(expected = DAOException.class)
	public void findByName3() throws Exception {
		dao.findByName(UNUSED_USER_ID, NAME);
	}

	/**
	 * non-existing name
	 */
	@Test(expected = DAOException.class)
	public void findByName4() throws Exception {
		dao.findByName(USER_ID, UNUSED_NAME);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws Exception {
		dao.create(file);
		assertEquals("Files are not same.", file, dao.findByName(file
				.getUserId(), file.getName()));
		assertEquals("User id and file name are not case insensitive", file,
				dao.findByName(file.getUserId().toUpperCase(), file.getName()
						.toUpperCase()));
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
		assertEquals("Not an empty list.", Collections.emptyList(), dao
				.findByUser(UNUSED_USER_ID));
	}

	/**
	 * everything ok. one file in collection
	 */
	@Test
	public void findByUser3() throws Exception {
		dao.create(file);
		List<UserFile> files = new ArrayList<UserFile>(1);
		files.add(file);
		assertEquals("Collections are not same.", files, dao.findByUser(file
				.getUserId()));
		assertEquals("User id is not case insensitive.", files, dao
				.findByUser(file.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two files in collection
	 */
	@Test
	public void findByUser4() throws Exception {
		UserFile newFile = new UserFile();
		newFile.setName(UNUSED_NAME);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setUserId(USER_ID);
		newFile.setContent("abc");
		dao.create(file);
		dao.create(newFile);
		List<UserFile> files = new ArrayList<UserFile>(2);
		files.add(file);
		files.add(newFile);
		assertEquals("Collections are not same.", files, dao.findByUser(file
				.getUserId()));
		assertEquals("User id is not case insensitive.", files, dao
				.findByUser(file.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two collections
	 */
	@Test
	public void findByUser5() throws Exception {
		UserFile newFile = new UserFile();
		newFile.setName(UNUSED_NAME);
		newFile.setType(FileTypes.VHDL_SOURCE);
		newFile.setUserId(UNUSED_USER_ID);
		newFile.setContent("abc");
		dao.create(file);
		dao.create(newFile);
		List<UserFile> collection1 = new ArrayList<UserFile>(1);
		List<UserFile> collection2 = new ArrayList<UserFile>(1);
		collection1.add(file);
		collection2.add(newFile);
		assertEquals("Collections are not same.", collection1, dao
				.findByUser(file.getUserId()));
		assertEquals("Collections are not same.", collection1, dao
				.findByUser(file.getUserId().toUpperCase()));
		assertEquals("User id is not case insensitive.", collection2, dao
				.findByUser(newFile.getUserId()));
		assertEquals("User id is not case insensitive.", collection2, dao
				.findByUser(newFile.getUserId().toUpperCase()));
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
			f.setName("name" + (i + 1));
			f.setUserId(USER_ID);
			f.setType(FileTypes.VHDL_SOURCE);
			f.setContent("abcdef");
			dao.create(f);
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
