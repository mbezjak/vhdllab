package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Resource} superclass entity.
 * 
 * @author Miro Bezjak
 */
public class ResourceTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "resource.name";
	private static final String TYPE = "resource.type";
	private static final String CONTENT = "...resource content...";
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new." + NAME;
	private static final String NEW_TYPE = "new." + TYPE;
	private static final String NEW_CONTENT = "new." + CONTENT;
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

	private Resource res;
	private Resource res2;

	@Before
	public void initEachTest() {
		res = new Resource();
		res.setId(ID);
		res.setName(NAME);
		res.setType(TYPE);
		res.setContent(CONTENT);
		res.setCreated(CREATED);
		res2 = new Resource(res);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue("same reference.", res != res2);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals("getId.", ID, res.getId());
		assertEquals("getName.", NAME, res.getName());
		assertEquals("getType.", TYPE, res.getType());
		assertEquals("getContent.", CONTENT, res.getContent());
		assertEquals("getCreated.", CREATED, res.getCreated());
	}

	/**
	 * Test equals with self, null, and non-resource object
	 */
	@Test
	public void equals() {
		assertEquals("not equal.", res, res);
		assertNotSame("resource is equal to null.", res, null);
		assertNotSame("can compare with string object.", res, "a string object");
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		res.compareTo(null);
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		res2.setName(NEW_NAME);
		res2.setType(NEW_TYPE);
		res2.setContent(NEW_CONTENT);
		res2.setCreated(NEW_CREATED);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Ids are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		res2.setId(NEW_ID);
		assertNotSame("equal.", res, res2);
		assertNotSame("hashCode same.", res.hashCode(), res2.hashCode());
		assertEquals("not compared by id.", ID.compareTo(NEW_ID) < 0 ? -1 : 1,
				res.compareTo(res2));
	}

	/**
	 * Ids are null, then name is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		res.setId(null);
		res2.setId(null);
		res2.setType(NEW_TYPE);
		res2.setContent(NEW_CONTENT);
		res2.setCreated(NEW_CREATED);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		res.setId(null);
		res2.setId(null);
		res2.setName(NAME.toUpperCase());
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		res.setId(null);
		res2.setId(null);
		res2.setName(NEW_NAME);
		assertNotSame("equal.", res, res2);
		assertNotSame("hashCode same.", res.hashCode(), res2.hashCode());
		assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, res.compareTo(res2));
	}

	/**
	 * Ids and names are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo6() {
		res.setId(null);
		res2.setId(null);
		res.setName(null);
		res2.setName(null);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(res.toString());
	}

}
