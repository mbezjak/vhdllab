package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.generateJunkString;
import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Project} entity.
 * 
 * @author Miro Bezjak
 */
public class ProjectTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "project.name";
	private static final String USER_ID = "user.identifier";
	private static final Date CREATED;
	private static final String NEW_NAME = "new.project.name";
	private static final String NEW_USER_ID = "new.user.identifier";
	private static final Date NEW_CREATED;

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
	}

	private Project project;
	private Project project2;
	private File file;

	@Before
	public void initEachTest() throws Exception {
		project = new Project(USER_ID, NAME);
		injectValueToPrivateField(project, "id", ID);
		injectValueToPrivateField(project, "created", CREATED);
		project2 = new Project(project);

		file = new File(project, "file.name", "file.type", "file.content");
		injectValueToPrivateField(file, "id", Long.valueOf(10));
		injectValueToPrivateField(file, "created", new Date());
		new File(file, project2);

		// not same because file was added when id was not set. so now it has
		// different hash code.
		assertNotSame("files are equal.", project.getChildren(), project2
				.getChildren());
		Set<File> set = new HashSet<File>();
		set.add(file);
		injectValueToPrivateField(project, "children", set);
		assertEquals("children not same.", project.getChildren(), project2
				.getChildren());
	}

	/**
	 * User id is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Project(null, NAME);
	}

	/**
	 * User id is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor2() throws Exception {
		new Project(generateJunkString(Project.USER_ID_LENGTH + 1), NAME);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() throws Exception {
		assertTrue("same reference.", project != project2);
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
		assertEquals("files not same.", project.getChildren(), project2
				.getChildren());
	}
	
	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals("getUserId.", USER_ID, project.getUserId());
	}

	/**
	 * Test equals with self, null, and non-project object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", project, project);
		assertNotSame("file is equal to null.", project, null);
		assertNotSame("can compare with string object.", project,
				"a string object");
		assertNotSame("can compare with resource object.", project,
				new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		project.compareTo(null);
	}

	/**
	 * Non-project type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() throws Exception {
		project.compareTo(new Container<File, Project>());
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() throws Exception {
		project2.setName(NEW_NAME);
		injectValueToPrivateField(project2, "userId", NEW_USER_ID);
		injectValueToPrivateField(project2, "created", NEW_CREATED);
		injectValueToPrivateField(project2, "children", new HashSet<File>(0));
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * Ids are null, then name and userId is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() throws Exception {
		injectValueToPrivateField(project, "id", null);
		injectValueToPrivateField(project2, "id", null);
		injectValueToPrivateField(project2, "created", NEW_CREATED);
		injectValueToPrivateField(project2, "children", new HashSet<File>(0));
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * User id is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() throws Exception {
		injectValueToPrivateField(project, "id", null);
		injectValueToPrivateField(project2, "id", null);
		injectValueToPrivateField(project2, "userId", USER_ID.toUpperCase());
		injectValueToPrivateField(project2, "children", new HashSet<File>(0));
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * Ids are null and user ids are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() throws Exception {
		injectValueToPrivateField(project, "id", null);
		injectValueToPrivateField(project2, "id", null);
		injectValueToPrivateField(project2, "userId", NEW_USER_ID);
		assertNotSame("equal.", project, project2);
		assertNotSame("hashCode same.", project.hashCode(), project2.hashCode());
		assertEquals("not compared by user id.",
				USER_ID.compareTo(NEW_USER_ID) < 0 ? -1 : 1, project
						.compareTo(project2));
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(project);
	}

}
