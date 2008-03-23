package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link BidiResource} class.
 * 
 * @author Miro Bezjak
 */
public class BidiResourceTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "file.name";
	private static final String TYPE = "file.type";
	private static final String CONTENT = "...file content...";
	private static final Date CREATED;

	static {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		try {
			CREATED = df.parse("2008-01-02 13-45");
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

	@Before
	public void initEachTest() throws Exception {
		con = new MockContainer("container1.name");
		con2 = new MockContainer(con);

		res = new MockResource(con, NAME, TYPE, CONTENT);
		injectValueToPrivateField(res, "id", ID);
		injectValueToPrivateField(res, "created", CREATED);
		res2 = new MockResource(res, con2);
	}

	/**
	 * Parent is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new MockResource(null, NAME, TYPE);
	}

	/**
	 * Parent is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructor2() throws Exception {
		new MockResource(null, NAME, TYPE, CONTENT);
	}

	/**
	 * Parent is not null.
	 */
	@Test
	public void constructor3() throws Exception {
		MockResource resource = new MockResource(con, NAME, TYPE, CONTENT);
		assertNotNull("parent is not set.", resource.getParent());
		assertEquals("non-expected parent is set.", con, resource.getParent());
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() throws Exception {
		assertTrue("same reference.", res != res2);
		assertEquals("not equal.", res, res2);
		assertEquals("hashCode not same.", res.hashCode(), res2.hashCode());
		assertEquals("not equal by compareTo.", 0, res.compareTo(res2));
	}

	/**
	 * Test references to parent
	 */
	@Test
	public void copyConstructor2() throws Exception {
		res2 = new MockResource(res, con2);
		assertNotNull("parent is not set.", res2.getParent());
		assertEquals("non-expected parent is set.", con, res2.getParent());
	}
	
	/**
	 * Disconnects a bidi-resource from container.
	 */
	@Test
	public void disconnect() throws Exception {
		res.disconnect();
		assertNull("resource is not disconnected.", res.getParent());
	}

	/**
	 * Test equals with self, null, and non-bidi-resource object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", res, res);
		assertNotSame("file is equal to null.", res, null);
		assertNotSame("can compare with string object.", res, "a string object");
		assertNotSame("can compare with resource object.", res, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		res.compareTo(null);
	}

	/**
	 * Non-bidi-resource type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() throws Exception {
		res.compareTo(new Resource());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(res.toString());
	}

}
