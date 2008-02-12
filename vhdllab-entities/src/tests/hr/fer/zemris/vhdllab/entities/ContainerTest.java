package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.generateJunkString;
import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
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
 * A test case for {@link Container} superclass entity.
 * 
 * @author Miro Bezjak
 */
public class ContainerTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "container.name";
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new." + NAME;
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

	private class MockResource extends
			BidiResource<MockContainer, MockResource> {
		private static final long serialVersionUID = 1L;

		public MockResource(MockContainer parent, String name, String type) {
			super(parent, name, type);
			parent.addChild(this);
		}

		public MockResource(MockContainer parent, String name, String type,
				String content) {
			super(parent, name, type, content);
			parent.addChild(this);
		}

		public MockResource(MockResource r, MockContainer parent) {
			super(r, parent);
			parent.addChild(this);
		}
	}

	private class MockContainer extends Container<MockResource, MockContainer> {
		private static final long serialVersionUID = 1L;

		public MockContainer(String name) {
			super(name);
		}

		public MockContainer(MockContainer c) {
			super(c);
		}
	}

	private MockContainer con;
	private MockContainer con2;
	private MockResource res;
	private MockResource res2;
	private Set<MockResource> children;

	@Before
	public void initEachTest() throws Exception {
		con = new MockContainer(NAME);
		injectValueToPrivateField(con, "id", ID);
		injectValueToPrivateField(con, "created", CREATED);
		con2 = new MockContainer(con);

		res = new MockResource(con, "resource.name", "resource.type",
				"resource.content");
		injectValueToPrivateField(res, "id", Long.valueOf(10));
		injectValueToPrivateField(res, "created", new Date());
		res2 = new MockResource(res, con2);

		// not same because resource was added when id was not set. so now it
		// has different hash code.
		assertNotSame("files are equal.", con.getChildren(), con2.getChildren());
		Set<MockResource> set = new HashSet<MockResource>();
		set.add(res);
		injectValueToPrivateField(con, "children", set);
		assertEquals("children not same.", con.getChildren(), con2
				.getChildren());

		children = new HashSet<MockResource>();
		children.add(res);
	}

	/**
	 * Name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new MockContainer((String) null);
	}

	/**
	 * Name is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor2() throws Exception {
		new MockContainer(generateJunkString(Container.NAME_LENGTH + 1));
	}

	/**
	 * Children and created date are not null
	 */
	@Test
	public void constructor3() throws Exception {
		MockContainer mock = new MockContainer(NAME);
		assertNotNull("created date is null.", mock.getCreated());
		assertNotNull("children is null.", mock.getChildren());
	}

	/**
	 * Container is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor() throws Exception {
		new MockContainer((MockContainer) null);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor2() throws Exception {
		assertTrue("same reference.", con != con2);
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
		assertEquals("children not same.", con.getChildren(), con2
				.getChildren());
	}

	/**
	 * Test references to children
	 */
	@Test
	public void copyConstructor3() throws Exception {
		con2 = new MockContainer(con);
		assertEquals("reference to children is copied.",
				new HashSet<MockResource>(0), con2.getChildren());
		assertNotNull("original children reference is missing.", con
				.getChildren());
		assertEquals("children has been modified.", children, con.getChildren());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals("getId.", ID, con.getId());
		assertEquals("getName.", NAME, con.getName());
		assertEquals("getCreated.", CREATED, con.getCreated());
		assertEquals("getChildren.", children, con.getChildren());
	}

	/**
	 * Name is null
	 */
	@Test(expected = NullPointerException.class)
	public void setName() throws Exception {
		con.setName(null);
	}

	/**
	 * Name is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setName2() throws Exception {
		con.setName(generateJunkString(Container.NAME_LENGTH + 1));
	}

	/**
	 * Everything is ok
	 */
	@Test
	public void setName3() throws Exception {
		con.setName(NEW_NAME);
		assertEquals("new name not set.", NEW_NAME, con.getName());
	}

	/**
	 * Test equals with self, null, and non-container object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", con, con);
		assertNotSame("file is equal to null.", con, null);
		assertNotSame("can compare with string object.", con, "a string object");
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		con.compareTo(null);
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() throws Exception {
		con2.setName(NEW_NAME);
		injectValueToPrivateField(con2, "created", NEW_CREATED);
		injectValueToPrivateField(con2, "children",
				new HashSet<MockResource>(0));
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Ids are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() throws Exception {
		injectValueToPrivateField(con2, "id", NEW_ID);
		assertNotSame("equal.", con, con2);
		assertNotSame("hashCode same.", con.hashCode(), con2.hashCode());
		assertEquals("not compared by id.", ID.compareTo(NEW_ID) < 0 ? -1 : 1,
				con.compareTo(con2));
	}

	/**
	 * Ids are null, then name is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() throws Exception {
		injectValueToPrivateField(con, "id", null);
		injectValueToPrivateField(con2, "id", null);
		injectValueToPrivateField(con2, "created", NEW_CREATED);
		injectValueToPrivateField(con2, "children",
				new HashSet<MockResource>(0));
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() throws Exception {
		injectValueToPrivateField(con, "id", NEW_ID);
		injectValueToPrivateField(con2, "id", NEW_ID);
		con2.setName(NAME.toUpperCase());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() throws Exception {
		injectValueToPrivateField(con, "id", null);
		injectValueToPrivateField(con2, "id", null);
		con2.setName(NEW_NAME);
		assertNotSame("equal.", con, con2);
		assertNotSame("hashCode same.", con.hashCode(), con2.hashCode());
		assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, con.compareTo(con2));
	}

	/**
	 * Resource is null
	 */
	@Test(expected = NullPointerException.class)
	public void addChild() throws Exception {
		con.addChild(null);
	}

	/**
	 * Add a resource
	 */
	@Test
	public void addChild2() throws Exception {
		con.addChild(res2);
		children.add(res2);
		assertTrue("child not added.", con.getChildren().contains(res2));
		assertEquals("children not same.", children, con.getChildren());
	}

	/**
	 * Add a resource that is already in that container
	 */
	@Test
	public void addChild3() throws Exception {
		con.addChild(res);
		assertEquals("children not same.", children, con.getChildren());
		assertEquals("children is changed.", 1, con.getChildren().size());
	}

	/**
	 * Resource is null
	 */
	@Test(expected = NullPointerException.class)
	public void removeChild() throws Exception {
		con.removeChild(null);
	}

	/**
	 * Remove a resource
	 */
	@Test
	public void removeChild2() throws Exception {
		con.removeChild(res);
		assertEquals("children not empty.", new HashSet<MockResource>(0), con
				.getChildren());
		assertNull("parent is not set to null.", res.getParent());
	}

	/**
	 * Remove a resource that does not exists in a container
	 */
	@Test
	public void removeChild3() throws Exception {
		MockContainer mockContainer = new MockContainer("mock.name");
		MockResource mockResource = new MockResource(mockContainer,
				"mock.name", "mock.type");
		con.removeChild(mockResource);
		assertEquals("children is changed.", children, con.getChildren());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(con.toString());
	}

}
