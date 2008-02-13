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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
		res2 = new MockResource(res, con2);

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
		assertEquals("children is not empty.", Collections.emptySet(), mock
				.getChildren());
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
				Collections.emptySet(), con2.getChildren());
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
	 * Only names are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() throws Exception {
		injectValueToPrivateField(con2, "id", NEW_ID);
		injectValueToPrivateField(con2, "created", NEW_CREATED);
		injectValueToPrivateField(con2, "children", Collections.emptySet());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Names are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() throws Exception {
		injectValueToPrivateField(con2, "name", NEW_NAME);
		assertNotSame("equal.", con, con2);
		assertNotSame("hashCode same.", con.hashCode(), con2.hashCode());
		assertEquals("not compared by id.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, con.compareTo(con2));
	}

	/**
	 * Name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() throws Exception {
		injectValueToPrivateField(con2, "name", NAME.toUpperCase());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
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
		assertEquals("children not empty.", Collections.emptySet(), con
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

	/**
	 * Test iterator on current collection
	 */
	@Test
	public void iterator() throws Exception {
		Iterator<MockResource> iterator = con.iterator();
		Iterator<MockResource> childrenIterator = con.getChildren().iterator();
		assertEquals("don't have next element.", childrenIterator.hasNext(),
				iterator.hasNext());
		assertEquals("next elements not equal.", childrenIterator.next(),
				iterator.next());
		assertEquals("next element exists.", childrenIterator.hasNext(),
				iterator.hasNext());
	}

	/**
	 * Test iterator after adding an element to collection
	 */
	@Test
	public void iterator2() throws Exception {
		new MockResource(con, "res.name", "res.type");
		Iterator<MockResource> iterator = con.iterator();
		Iterator<MockResource> childrenIterator = con.getChildren().iterator();
		assertEquals("don't have next element.", childrenIterator.hasNext(),
				iterator.hasNext());
		assertEquals("next elements not equal.", childrenIterator.next(),
				iterator.next());
		assertEquals("next element exists.", childrenIterator.hasNext(),
				iterator.hasNext());
	}

	/**
	 * Remove an element using iterator
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void iterator3() throws Exception {
		Iterator<MockResource> iterator = con.iterator();
		assertTrue("element missing.", iterator.hasNext());
		assertEquals("wrong element.", res, iterator.next());
		iterator.remove();
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(con.toString());
	}

}
