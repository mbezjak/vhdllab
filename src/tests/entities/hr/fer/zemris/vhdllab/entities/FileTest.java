package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link File} entity.
 * 
 * @author Miro Bezjak
 */
public class FileTest {
	
	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "file.name";
	private static final String TYPE = "file.type";
	private static final String CONTENT = "...file content...";
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new.file.name";
	private static final String NEW_TYPE = "new.file.type";
	private static final String NEW_CONTENT = "...new file content...";
	
	private Project project;
	private Project project2;
	private File file;
	private File file2;
	
	@Before
	public void initEachTest() {
		file = new File();
		file.setId(ID);
		file.setName(NAME);
		file.setType(TYPE);
		file.setContent(CONTENT);
		project = new Project();
		project.setId(Long.valueOf(987));
		project.setName("project.name");
		project.setUserId("user.identifier");
		project.addFile(file);
		file2 = new File(file);
		project2 = new Project();
		project2.setId(Long.valueOf(777888999));
		project2.setName("another project name");
		project2.setUserId("another user identifier");
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
	 * File's project is null
	 */
	@Test
	public void copyConstructor2() {
		file.setProject(null);
		file2 = new File(file);
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
		assertEquals(TYPE, file.getType());
		assertEquals(CONTENT, file.getContent());
		assertEquals(project, file.getProject());
	}
	
	/**
	 * Test equals with self, null, and non-file object
	 */
	@Test
	public void equals() {
		assertEquals(file, file);
		assertNotSame(file, null);
		assertNotSame(file, NAME);
	}
	
	/**
	 * Only ids (if set) are important in equals, hashcode and compareTo
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo() {
		file2.setName(NEW_NAME);
		file2.setType(NEW_TYPE);
		file2.setContent(NEW_CONTENT);
		file2.setProject(project2);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Ids are different
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo2() {
		file2.setId(NEW_ID);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
		assertNotSame(0, file.compareTo(file2));
	}
	
	/**
	 * Ids are null, then project and name is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo3() {
		file.setId(null);
		file2.setId(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * File name is case insensitive
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo4() {
		file.setId(null);
		file2.setId(null);
		file2.setName(NAME.toUpperCase());
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo5() {
		file.setId(null);
		file2.setId(null);
		file2.setName(NEW_NAME);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
		assertNotSame(0, file.compareTo(file2));
	}
	
	/**
	 * Ids are null and projects are not equal
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo6() {
		file.setId(null);
		file2.setId(null);
		file2.setProject(project2);
		assertNotSame(file, file2);
		assertNotSame(file.hashCode(), file2.hashCode());
		assertNotSame(0, file.compareTo(file2));
	}
	
	/**
	 * Ids and names are null, then project is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo7() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Ids and projects are null then name is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo8() {
		file.setId(null);
		file2.setId(null);
		file.setProject(null);
		file2.setProject(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	/**
	 * Ids, projects and names are null
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo9() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		file.setProject(null);
		file2.setProject(null);
		assertEquals(file, file2);
		assertEquals(file.hashCode(), file2.hashCode());
		assertEquals(0, file.compareTo(file2));
	}
	
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(file);
	}
	
}
