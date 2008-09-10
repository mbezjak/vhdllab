package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.generateJunkString;
import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
	public void initEachTest() throws Exception {
		res = new Resource(NAME, TYPE, CONTENT);
		injectValueToPrivateField(res, "id", ID);
		injectValueToPrivateField(res, "created", CREATED);
		res2 = new Resource(res);
	}

	/**
	 * Name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Resource(null, TYPE);
	}

	/**
	 * Type is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new Resource(NAME, null);
	}

	/**
	 * Name is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor3() throws Exception {
		new Resource(generateJunkString(Resource.NAME_LENGTH + 1), TYPE);
	}

	/**
	 * Type is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor4() throws Exception {
		new Resource(NAME, generateJunkString(Resource.TYPE_LENGTH + 1));
	}

	/**
	 * Content and created date is not null
	 */
	@Test
	public void constructor5() throws Exception {
		Resource resource = new Resource(NAME, TYPE);
		assertNotNull("content is null.", resource.getContent());
		assertNotNull("created date is null.", resource.getCreated());
	}

	/**
	 * Name is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor6() throws Exception {
		new Resource(null, TYPE, CONTENT);
	}

	/**
	 * Type is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor7() throws Exception {
		new Resource(NAME, null, CONTENT);
	}

	/**
	 * Content is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor8() throws Exception {
		new Resource(NAME, TYPE, null);
	}

	/**
	 * Name is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor9() throws Exception {
		new Resource(generateJunkString(Resource.NAME_LENGTH + 1), TYPE,
				CONTENT);
	}

	/**
	 * Type is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor10() throws Exception {
		new Resource(NAME, generateJunkString(Resource.TYPE_LENGTH + 1),
				CONTENT);
	}

	/**
	 * Content is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor11() throws Exception {
		new Resource(NAME, TYPE,
				generateJunkString(Resource.CONTENT_LENGTH + 1));
	}

	/**
	 * Created date is not null
	 */
	@Test
	public void constructor12() throws Exception {
		Resource resource = new Resource(NAME, TYPE, CONTENT);
		assertNotNull("created date is null.", resource.getCreated());
	}

	/**
	 * Created date depends on real time
	 */
	@Test
	public void constructor13() throws Exception {
		Resource resource = new Resource(NAME, TYPE, CONTENT);
		Thread.sleep(1, 0); // sleep 1 ms
		Resource resource2 = new Resource(NAME, TYPE, CONTENT);
		assertTrue("created date doesn't depend on real time.", resource2
				.getCreated().compareTo(resource.getCreated()) > 0);
	}

	/**
	 * Resource is null
	 */
	@Test(expected = NullPointerException.class)
	public void copyConstructor() throws Exception {
		new Resource(null);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor2() throws Exception {
		assertTrue("same reference.", res != res2);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
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
	 * getCreated returns a defensive copy of a date
	 */
	@Test
	public void getCreated() throws Exception {
		Date originalDate = new Date(res.getCreated().getTime());
		res.getCreated().setTime(0);
		assertEquals("created date is mutable.", originalDate, res.getCreated());
	}

	/**
	 * Content is null
	 */
	@Test(expected = NullPointerException.class)
	public void setContent() throws Exception {
		res.setContent(null);
	}

	/**
	 * Content is too long
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setContent2() throws Exception {
		res.setContent(generateJunkString(Resource.CONTENT_LENGTH + 1));
	}

	/**
	 * Everything is ok
	 */
	@Test
	public void setContent3() throws Exception {
		res.setContent(NEW_CONTENT);
		assertEquals("new content not set.", NEW_CONTENT, res.getContent());
	}

	/**
	 * Test equals with self, null, and non-resource object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", res, res);
        assertFalse("resource is equal to null.", res.equals(null));
        assertFalse("can compare with string object.", res
                .equals(new Resource()));
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		res.compareTo(null);
	}

	/**
	 * Only names are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() throws Exception {
		res2 = new Resource(NAME, NEW_TYPE, NEW_CONTENT);
		injectValueToPrivateField(res2, "id", NEW_ID);
		injectValueToPrivateField(res2, "created", NEW_CREATED);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Names are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() throws Exception {
		injectValueToPrivateField(res2, "name", NEW_NAME);
        assertFalse("equal.", res.equals(res2));
        assertFalse("hashCode same.", res.hashCode() == res2.hashCode());
		assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
				: 1, res.compareTo(res2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() throws Exception {
		injectValueToPrivateField(res2, "name", NAME.toUpperCase());
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
