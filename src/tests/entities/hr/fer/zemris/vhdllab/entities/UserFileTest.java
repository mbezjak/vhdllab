package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link UserFile} entity.
 * 
 * @author Miro Bezjak
 */
public class UserFileTest {
	
	private static final Long ID = Long.valueOf(123456);
	private static final String USER_ID = "user.identifier";
	private static final String NAME = "file.name";
	private static final String CONTENT = "...file content...";
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_USER_ID = "new.user.identifier";
	private static final String NEW_NAME = "new.file.name";
	private static final String NEW_CONTENT = "...new file content...";
	
	private UserFile file;
	private UserFile file2;
	
	@Before
	public void initEachTest() {
		file = new UserFile();
		file.setId(ID);
		file.setUserId(USER_ID);
		file.setName(NAME);
		file.setContent(CONTENT);
		file2 = new UserFile(file);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue(file != file2);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		assertEquals(ID, file.getId());
		assertEquals(USER_ID, file.getUserId());
		assertEquals(NAME, file.getName());
		assertEquals(CONTENT, file.getContent());
	}
	
	/**
	 * Test equals with self, null, and non-user-file object
	 */
	@Test
	public void equals() {
		assertEquals(file, file);
		assertNotSame(file, null);
		assertNotSame(file, NAME);
	}
	
	/**
	 * Only ids (if set) are important in equals and hashcode
	 */
	@Test
	public void equalsAndHashCode() {
		file2.setUserId(NEW_USER_ID);
		file2.setName(NEW_NAME);
		file2.setContent(NEW_CONTENT);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids are different
	 */
	@Test
	public void equalsAndHashCode2() {
		file2.setId(NEW_ID);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids are null, then userId and name is important
	 */
	@Test
	public void equalsAndHashCode3() {
		file.setId(null);
		file2.setId(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * File name is case insensitive
	 */
	@Test
	public void equalsAndHashCode4() {
		file.setId(null);
		file2.setId(null);
		file2.setName(NAME.toUpperCase());
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * User id is case insensitive
	 */
	@Test
	public void equalsAndHashCode5() {
		file.setId(null);
		file2.setId(null);
		file2.setUserId(USER_ID.toUpperCase());
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsAndHashCode6() {
		file.setId(null);
		file2.setId(null);
		file2.setName(NEW_NAME);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids are null and userIds are not equal
	 */
	@Test
	public void equalsAndHashCode7() {
		file.setId(null);
		file2.setId(null);
		file2.setUserId(NEW_USER_ID);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids and names are null, then user id is important
	 */
	@Test
	public void equalsAndHashCode8() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids and user ids are null, then name is important
	 */
	@Test
	public void equalsAndHashCode9() {
		file.setId(null);
		file2.setId(null);
		file.setUserId(null);
		file2.setUserId(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids, user ids and names are null
	 */
	@Test
	public void equalsAndHashCode10() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		file.setUserId(null);
		file2.setUserId(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(file);
	}
	
}
