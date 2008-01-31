package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	private class CustomResource extends
			BidiResource<CustomContainer, CustomResource> {
		private static final long serialVersionUID = 1L;

		public CustomResource() {
			super();
		}

		public CustomResource(CustomResource r) {
			super(r);
		}
	}

	private class CustomContainer extends
			Container<CustomResource, CustomContainer> {
		private static final long serialVersionUID = 1L;

		public CustomContainer() {
			super();
		}

		public CustomContainer(CustomContainer c) {
			super(c);
		}
	}

	private CustomContainer con;
	private CustomContainer con2;
	private CustomResource res;
	private CustomResource res2;
	private Set<CustomResource> children;

	@Before
	public void initEachTest() {
		con = new CustomContainer();
		con.setId(ID);
		con.setName(NAME);
		con.setCreated(CREATED);
		con2 = new CustomContainer(con);

		res = new CustomResource();
		res.setId(Long.valueOf(10));
		res.setName("resource1.name");
		res.setType("resource1.type");
		res.setContent("resource1.content");
		res.setCreated(Calendar.getInstance().getTime());
		res2 = new CustomResource(); // not added immediately to container
		res2.setId(Long.valueOf(20));
		res2.setName("resource2.name");
		res2.setType("resource2.type");
		res2.setContent("resource2.content");
		res2.setCreated(Calendar.getInstance().getTime());
		CustomResource resourceDuplicate = new CustomResource(res);

		con.addChild(res);
		con2.addChild(resourceDuplicate);
		children = new HashSet<CustomResource>();
		children.add(res);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
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
	public void copyConstructor2() {
		con2 = new CustomContainer(con);
		assertEquals("reference to children is copied.",
				new HashSet<CustomResource>(0), con2.getChildren());
		assertNotNull("original children reference is missing.", con
				.getChildren());
		assertEquals("children has been modified.", children, con.getChildren());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
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
	public void equals() {
		assertEquals("not equal.", con, con);
		assertNotSame("file is equal to null.", con, null);
		assertNotSame("can compare with string object.", con, "a string object");
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		con.compareTo(null);
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		con2.setName(NEW_NAME);
		con2.setCreated(NEW_CREATED);
		con2.setChildren(new HashSet<CustomResource>());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Ids are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		con2.setId(NEW_ID);
		assertNotSame("equal.", con, con2);
		assertNotSame("hashCode same.", con.hashCode(), con2.hashCode());
		assertEquals("not compared by id.", ID.compareTo(NEW_ID) < 0 ? -1 : 1,
				con.compareTo(con2));
	}

	/**
	 * Ids are null, then name is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		con.setId(null);
		con2.setId(null);
		con2.setCreated(NEW_CREATED);
		con2.setChildren(new HashSet<CustomResource>());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		con.setId(null);
		con2.setId(null);
		con2.setName(NAME.toUpperCase());
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		con.setId(null);
		con2.setId(null);
		con2.setName(NEW_NAME);
		assertNotSame("equal.", con, con2);
		assertNotSame("hashCode same.", con.hashCode(), con2.hashCode());
		assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, con.compareTo(con2));
	}

	/**
	 * Ids and names are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo10() {
		con.setId(null);
		con2.setId(null);
		con.setName(null);
		con2.setName(null);
		assertEquals("not equal.", con, con2);
		assertEquals("hashCode not same.", con.hashCode(), con2.hashCode());
		assertEquals("not equal by compareTo.", 0, con.compareTo(con2));
	}

	/**
	 * Resource is null
	 */
	@Test(expected = NullPointerException.class)
	public void addChild() {
		con.addChild(null);
	}

	/**
	 * Add a resource
	 */
	@Test
	public void addChild2() {
		con.addChild(res2);
		children.add(res2);
		assertTrue("child not added.", con.getChildren().contains(res2));
		assertEquals("children not same.", children, con.getChildren());
	}

	/**
	 * Add a resource that is already in another container
	 */
	@Test
	public void addChild3() {
		CustomContainer newContainer = new CustomContainer();
		newContainer.setId(NEW_ID);
		newContainer.setName(NEW_NAME);
		newContainer.setCreated(NEW_CREATED);
		newContainer.addChild(res);
		assertEquals("child not added.", children, newContainer.getChildren());
	}

	/**
	 * Add a resource that is already in that container
	 */
	@Test
	public void addChild4() {
		con.addChild(res);
		assertEquals("children not same.", children, con.getChildren());
		assertEquals("children is changed.", 1, con.getChildren().size());
	}

	/**
	 * Resource is null
	 */
	@Test(expected = NullPointerException.class)
	public void removeChild() {
		con.removeChild(null);
	}

	/**
	 * Remove a resource
	 */
	@Test
	public void removeChild2() {
		con.removeChild(res);
		assertEquals("children not empty.", new HashSet<CustomResource>(0), con
				.getChildren());
		assertNull("parent is not set to null.", res.getParent());
	}

	/**
	 * Remove a resource that does not exists in a container
	 */
	@Test
	public void removeChild3() {
		con2.getChildren().clear();
		con2.addChild(res2);
		con.removeChild(res2);
		assertEquals("children is changed.", children, con.getChildren());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(con.toString());
	}

}
