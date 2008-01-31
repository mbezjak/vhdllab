package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	@Before
	public void initEachTest() {
		res = new CustomResource();
		res.setId(ID);
		res.setName(NAME);
		res.setType(TYPE);
		res.setContent(CONTENT);
		res.setCreated(CREATED);
		res2 = new CustomResource(res);

		con = new CustomContainer();
		con.setId(Long.valueOf(10));
		con.setName("container1.name");
		con.setCreated(Calendar.getInstance().getTime());
		con2 = new CustomContainer(con);

		con.addChild(res);
		con2.addChild(res2);
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
	 * Test references to parent
	 */
	@Test
	public void copyConstructor2() {
		res2 = new CustomResource(res);
		assertNull("reference to parent is copied.", res2.getParent());
	}

	/**
	 * Test equals with self, null, and non-bidi-resource object
	 */
	@Test
	public void equals() {
		assertEquals("not equal.", res, res);
		assertNotSame("file is equal to null.", res, null);
		assertNotSame("can compare with string object.", res, "a string object");
		assertNotSame("can compare with resource object.", res, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		res.compareTo(null);
	}

	/**
	 * Non-bidi-resource type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() {
		res.compareTo(new Resource());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(res.toString());
	}

}
