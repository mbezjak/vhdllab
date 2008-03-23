package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

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
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new." + NAME;
	private static final String NEW_TYPE = "new." + TYPE;
	private static final String NEW_CONTENT = "new." + CONTENT;
	private static final Date NEW_CREATED;
	private static final Project NEW_PROJECT;

	static {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		try {
			CREATED = df.parse("2008-01-02 13-45");
			NEW_CREATED = df.parse("2000-12-31 07-13");
		} catch (ParseException e) {
			// should never happen. but if pattern should change report it by
			// throwing exception.
			throw new IllegalStateException(e);
		}
		NEW_PROJECT = new Project("new.user.identifier", "new.project.name");
		try {
			injectValueToPrivateField(NEW_PROJECT, "id", Long.valueOf(5555));
		} catch (Exception e) {
			// should never happen.
			throw new IllegalStateException(e);
		}
	}

	private Project project;
	private Project project2;
	private File file;
	private File file2;

	@Before
	public void initEachTest() throws Exception {
		project = new Project("user.identifier", "project.name");
		project2 = new Project(project);

		file = new File(project, NAME, TYPE, CONTENT);
		injectValueToPrivateField(file, "id", ID);
		injectValueToPrivateField(file, "created", CREATED);
		file2 = new File(file, project2);
	}

	/**
	 * Test references to project and project references back to file
	 */
	@Test
	public void constructor() throws Exception {
		File newFile = new File(NEW_PROJECT, NAME, TYPE);
		assertNotNull("project reference not copied.", newFile.getProject());
		assertEquals(NEW_PROJECT, newFile.getProject());
		assertTrue(NEW_PROJECT.getChildren().contains(newFile));
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() throws Exception {
		assertTrue("same reference.", file != file2);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Test references to project and project references back to file
	 */
	@Test
	public void copyConstructor2() throws Exception {
		File newFile = new File(file, NEW_PROJECT);
		assertNotNull("project reference not copied.", newFile.getProject());
		assertEquals(NEW_PROJECT, newFile.getProject());
		assertTrue(NEW_PROJECT.getChildren().contains(newFile));
	}

	/**
	 * Test if returned set is modifiable
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void getLibraryFiles() throws Exception {
		Set<File> files = project.getFiles();
		Project newProject = new Project("new.user.id", "new.project.name");
		File newFile = new File(newProject, "new.file.name", "new.file.type");
		files.add(newFile);
	}

	/**
	 * Test equals with self, null, and non-file object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", file, file);
		assertNotSame("file is equal to null.", file, null);
		assertNotSame("can compare with string object.", file,
				"a string object");
		assertNotSame("can compare with resource object.", file, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		file.compareTo(null);
	}

	/**
	 * Non-file type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() throws Exception {
		file.compareTo(new Resource());
	}

	/**
	 * Only projects and names are important in equals, hashcode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() throws Exception {
		injectValueToPrivateField(file2, "id", NEW_ID);
		injectValueToPrivateField(file2, "type", NEW_TYPE);
		injectValueToPrivateField(file2, "content", NEW_CONTENT);
		injectValueToPrivateField(file2, "created", NEW_CREATED);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Names are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() throws Exception {
		injectValueToPrivateField(file2, "name", NEW_NAME);
		assertNotSame("equal.", file, file2);
		assertNotSame("hashCode same.", file.hashCode(), file2.hashCode());
		assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, file.compareTo(file2));
	}

	/**
	 * Projects are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() throws Exception {
		File newFile = new File(NEW_PROJECT, NAME, TYPE, CONTENT);
		injectValueToPrivateField(file2, "id", ID);
		injectValueToPrivateField(file2, "created", CREATED);
		assertNotSame("equal.", file, newFile);
		assertNotSame("hashCode same.", file.hashCode(), newFile.hashCode());
		assertEquals("not compared by project.", file.getProject().compareTo(
				newFile.getProject()) < 0 ? -1 : 1, file.compareTo(newFile));
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(file);
	}

}
