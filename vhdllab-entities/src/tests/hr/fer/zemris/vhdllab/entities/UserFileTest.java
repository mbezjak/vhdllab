package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Resource} entity.
 * 
 * @author Miro Bezjak
 */
public class UserFileTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String PARENT = "parent.container";
	private static final String NAME = "resource.name";
	private static final String TYPE = "resource.type";
	private static final String CONTENT = "...resource content...";
	private static final Date CREATED = Date.valueOf("2008-01-02");
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_PARENT = "new." + PARENT;
	private static final String NEW_NAME = "new." + NAME;
	private static final String NEW_TYPE = "new." + TYPE;
	private static final String NEW_CONTENT = "new." + CONTENT;
	private static final Date NEW_CREATED = Date.valueOf("2000-12-31");

	private Resource<String> resource;
	private Resource<String> resource2;

	@Before
	public void initEachTest() {
		resource = new Resource<String>();
		resource.setId(ID);
		resource.setParent(PARENT);
		resource.setName(NAME);
		resource.setType(TYPE);
		resource.setContent(CONTENT);
		resource.setCreated(CREATED);
		resource2 = new Resource<String>(resource);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue(resource != resource2);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals(ID, resource.getId());
		assertEquals(PARENT, resource.getParent());
		assertEquals(NAME, resource.getName());
		assertEquals(TYPE, resource.getType());
		assertEquals(CONTENT, resource.getContent());
		assertEquals(CREATED, resource.getCreated());
	}

	/**
	 * Test equals with self, null, and non-resource object
	 */
	@Test
	public void equals() {
		assertEquals(resource, resource);
		assertNotSame(resource, null);
		assertNotSame(resource, "a string object");
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		resource2.setParent(NEW_PARENT);
		resource2.setName(NEW_NAME);
		resource2.setType(NEW_TYPE);
		resource2.setContent(NEW_CONTENT);
		resource2.setCreated(NEW_CREATED);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Ids are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		resource2.setId(NEW_ID);
		assertNotSame(resource, resource2);
		assertNotSame(resource.hashCode(), resource2.hashCode());
		assertEquals(ID.compareTo(NEW_ID) < 0 ? -1 : 1, resource.compareTo(resource2));
	}

	/**
	 * Ids are null, then name and type is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		resource.setId(null);
		resource2.setId(null);
		resource.setParent(NEW_PARENT);
		resource2.setParent(NEW_PARENT);
		resource.setContent(NEW_CONTENT);
		resource2.setContent(NEW_CONTENT);
		resource.setCreated(NEW_CREATED);
		resource2.setCreated(NEW_CREATED);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		resource.setId(null);
		resource2.setId(null);
		resource2.setName(NAME.toUpperCase());
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Resource type is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		resource.setId(null);
		resource2.setId(null);
		resource2.setType(TYPE.toUpperCase());
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo6() {
		resource.setId(null);
		resource2.setId(null);
		resource2.setName(NEW_NAME);
		assertNotSame(resource, resource2);
		assertNotSame(resource.hashCode(), resource2.hashCode());
		assertEquals(NAME.compareTo(NEW_NAME) < 0 ? -1 : 1, resource.compareTo(resource2));
	}

	/**
	 * Ids are null and types are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo7() {
		resource.setId(null);
		resource2.setId(null);
		resource2.setType(NEW_TYPE);
		assertNotSame(resource, resource2);
		assertNotSame(resource.hashCode(), resource2.hashCode());
		assertEquals(TYPE.compareTo(NEW_TYPE) < 0 ? -1 : 1, resource.compareTo(resource2));
	}

	/**
	 * Ids and names are null, then type is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo8() {
		resource.setId(null);
		resource2.setId(null);
		resource.setName(null);
		resource2.setName(null);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Ids and type are null, then name is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo9() {
		resource.setId(null);
		resource2.setId(null);
		resource.setType(null);
		resource2.setType(null);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	/**
	 * Ids, types and names are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo10() {
		resource.setId(null);
		resource2.setId(null);
		resource.setName(null);
		resource2.setName(null);
		resource.setType(null);
		resource2.setType(null);
		assertEquals(resource, resource2);
		assertEquals(resource.hashCode(), resource2.hashCode());
		assertEquals(0, resource.compareTo(resource2));
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(resource.toString());
	}

}
