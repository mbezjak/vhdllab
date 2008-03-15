package hr.fer.zemris.vhdllab.server.vhdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Range}.
 * 
 * @author Miro Bezjak
 */
public class RangeTest {

	private static final int FROM = 4;
	private static final int TO = 3;
	private static final VectorDirection DIRECTION = VectorDirection.DOWNTO;

	private Range range;

	@Before
	public void initEachTest() {
		range = new Range(FROM, DIRECTION, TO);
	}

	/**
	 * Scalar is a singleton
	 */
	@Test
	public void scalar() throws Exception {
		Range scalar = Range.SCALAR;
		assertTrue("scalar range is not a scalar.", scalar.isScalar());
		assertFalse("scalar range is a vector.", scalar.isVector());
		Range scalar2 = Range.SCALAR;
		assertTrue("scalar is not a singleton.", scalar == scalar2);
		assertEquals("scalars are not same.", scalar, scalar2);
		assertEquals("scalars are not same.", scalar.hashCode(), scalar2
				.hashCode());
	}

	/**
	 * Direction is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() throws Exception {
		new Range(FROM, null, TO);
	}

	/**
	 * from is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor2() throws Exception {
		new Range(-1, DIRECTION, TO);
	}

	/**
	 * to is negative
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor3() throws Exception {
		new Range(FROM, DIRECTION, -1);
	}

	/**
	 * from < to and direction is DOWNTO
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor4() throws Exception {
		new Range(1, VectorDirection.DOWNTO, 2);
	}

	/**
	 * from > to and direction is TO
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor5() throws Exception {
		new Range(3, VectorDirection.TO, 2);
	}

	/**
	 * from == to and direction is DOWNTO - no exception thrown
	 */
	@Test
	public void fromEqualTo() throws Exception {
		Range newRange = new Range(3, VectorDirection.DOWNTO, 3);
		assertEquals("bounds not equal.", newRange.getFrom(), newRange.getTo());
	}

	/**
	 * from == to and direction is TO - no exception thrown
	 */
	@Test
	public void fromEqualTo2() throws Exception {
		Range newRange = new Range(3, VectorDirection.TO, 3);
		assertEquals("bounds not equal.", newRange.getFrom(), newRange.getTo());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() throws Exception {
		assertEquals("getFrom", FROM, range.getFrom());
		assertEquals("getTo", TO, range.getTo());
		assertEquals("getDirection", DIRECTION, range.getDirection());
		assertTrue("range not a vector.", range.isVector());
	}

	/**
	 * Test equals with self, null, and non-range object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", range, range);
		assertNotSame("range is equal to null.", range, null);
		assertNotSame("can compare with string object.", range,
				"a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		Range newRange = new Range(range.getFrom(), range.getDirection(), range
				.getTo());

		assertEquals("range not equal", range, newRange);
		assertEquals("range not equal", range.hashCode(), newRange.hashCode());
	}

	/**
	 * from is different
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		Range newRange = new Range(10, range.getDirection(), range.getTo());

		assertNotSame("range are equal", range, newRange);
		assertNotSame("range are equal", range.hashCode(), newRange.hashCode());
	}

	/**
	 * to is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		Range newRange = new Range(range.getFrom(), range.getDirection(), 1);

		assertNotSame("range are equal", range, newRange);
		assertNotSame("range are equal", range.hashCode(), newRange.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = IllegalArgumentException.class)
	public void readResolve() throws Exception {
		Field field = range.getClass().getDeclaredField("from");
		field.setAccessible(true);
		field.set(range, -1); // set illegal state of result object

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(range);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		bis.readObject();
	}

	/**
	 * Serialize then deserialize
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void readResolve2() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(range);

		ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
				bos.toByteArray()));
		Range newRange = (Range) bis.readObject();

		assertEquals("directions are not equal.", range.getDirection(),
				newRange.getDirection());
		assertEquals("ranges not equal", range, newRange);
	}

	/**
	 * Range is a vector.
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(range.toString());
	}

	/**
	 * Range is a scalar.
	 */
	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString2() {
		System.out.println(Range.SCALAR.toString());
	}
	
}
