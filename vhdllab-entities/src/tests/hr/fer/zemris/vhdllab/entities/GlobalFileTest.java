package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link GlobalFile} entity.
 * 
 * @author Miro Bezjak
 */
public class GlobalFileTest {
	
	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "file.name";
	private static final String CONTENT = "...file content...";
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new.file.name";
	private static final String NEW_CONTENT = "...new file content...";
	
	private GlobalFile file;
	private GlobalFile file2;
	
	@Before
	public void initEachTest() {
		file = new GlobalFile();
		file.setId(ID);
		file.setName(NAME);
		file.setContent(CONTENT);
		file2 = new GlobalFile(file);
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
		assertEquals(NAME, file.getName());
		assertEquals(CONTENT, file.getContent());
	}
	
	/**
	 * Test equals with self, null, and non-global-file object
	 */
	@Test
	public void equals() {
		assertEquals(file, file);
		assertNotSame(file, null);
		assertNotSame(file, NAME);
	}
	
	/**
	 * Only id (if set) is important in equals and hashcode
	 */
	@Test
	public void equalsAndHashCode() {
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
	 * Ids are null, then name is important
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
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsAndHashCode5() {
		file.setId(null);
		file2.setId(null);
		file2.setName(NEW_NAME);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
	}
	
	/**
	 * Ids and names are null, then user id is important
	 */
	@Test
	public void equalsAndHashCode6() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(file);
	}

}
