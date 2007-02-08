package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultTypeTest {
	
	private static Type type;
	private static Type type2;
	
	@BeforeClass
	public static void init() {
		type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Name is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor() {
		new DefaultType(null, DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
	}
	
	/**
	 * Name is not of correct format.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor2() {
		new DefaultType("1std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
	}
	
	/**
	 * Range is describes a scalar while vector direction describes a vector.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor3() {
		new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Vector direction describes a vector while range describes a scalar.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor4() {
		new DefaultType("std_logic", new int[] {2, 0}, DefaultType.SCALAR_VECTOR_DIRECTION);
	}
	
	/**
	 * Range contains more then two elements.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor5() {
		new DefaultType("std_logic_vector", new int[] {2, 0, 1}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Range contains at least one negative element.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor6() {
		new DefaultType("std_logic_vector", new int[] {-2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Vector direction is incorrect.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor7() {
		new DefaultType("std_logic_vector", new int[] {2, 0}, "UPTO");
	}
	
	/**
	 * Range elements are not consistent with vector direction.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor8() {
		new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_TO);
	}
	
	/**
	 * Range elements are not consistent with vector direction.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor9() {
		new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Vector bounds are 0, 0.
	 */
	@Test
	public void constructor10() {
		new DefaultType("std_logic_vector", new int[] {0, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Range and vector direction match.
	 */
	@Test
	public void constructor11() {
		new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}

	/**
	 * Types are equal and port is a scalar.
	 */
	@Test
	public void equalsAndHashCode() {
		Type type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		Type type2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Types are equal and port is a vector.
	 */
	@Test
	public void equalsAndHashCode2() {
		Type type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		Type type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Types are not equal and port is a scalar.
	 */
	@Test
	public void equalsAndHashCode3() {
		Type type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		Type type2 = new DefaultType("std_logic2", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(false, type1.equals(type2));
		assertNotSame(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Types are not equal and port is a vector.
	 */
	@Test
	public void equalsAndHashCode4() {
		Type type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		Type type2 = new DefaultType("std_logic_vector2", new int[] {0, 2}, DefaultType.VECTOR_DIRECTION_TO);
		assertEquals(false, type1.equals(type2));
		assertNotSame(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Types are equal and port is a scalar.
	 */
	@Test
	public void equalsAndHashCode5() {
		Type type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		Type type2 = new DefaultType("STD_LOGIC", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Types are equal and port is a vector.
	 */
	@Test
	public void equalsAndHashCode6() {
		Type type1 = new DefaultType("std_logic", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		Type type2 = new DefaultType("STD_LOGIC", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		assertEquals(false, type.equals(null));
	}
	
	/**
	 * Object is not instance of Type.
	 */
	@Test
	public void equalsObject2() {
		assertEquals(false, type.equals(new String("std_logic")));
	}
	
	/**
	 * Port is a scalar.
	 */
	@Test(expected=IllegalStateException.class)
	public void getRangeFrom() {
		type.getRangeFrom();
	}
	
	/**
	 * Port is a vector.
	 */
	@Test
	public void getRangeFrom2() {
		assertEquals(2, type2.getRangeFrom());
	}

	/**
	 * Port is a scalar.
	 */
	@Test(expected=IllegalStateException.class)
	public void getRangeTo() {
		type.getRangeTo();
	}
	
	/**
	 * Port is a vector.
	 */
	@Test
	public void getRangeTo2() {
		assertEquals(0, type2.getRangeTo());
	}

	/**
	 * Port is a scalar.
	 */
	@Test
	public void isScalar() {
		assertEquals(true, type.isScalar());
	}
	
	/**
	 * Port is not a scalar.
	 */
	@Test
	public void isScalar2() {
		assertEquals(false, type2.isScalar());
	}

	/**
	 * Port is a vector.
	 */
	@Test
	public void isVector() {
		assertEquals(true, type2.isVector());
	}
	
	/**
	 * Port is not a vector.
	 */
	@Test
	public void isVector2() {
		assertEquals(false, type.isVector());
	}

	/**
	 * Port is a vector.
	 */
	@Test
	public void hasVectorDirectionDOWNTO() {
		assertEquals(true, type2.hasVectorDirectionDOWNTO());
	}
	
	/**
	 * Port is not a vector.
	 */
	@Test
	public void hasVectorDirectionDOWNTO2() {
		assertEquals(false, type.hasVectorDirectionDOWNTO());
	}

	/**
	 * Port is a vector.
	 */
	@Test
	public void hasVectorDirectionTO() {
		Type type = new DefaultType("std_logic_vector", new int[] {0, 2}, DefaultType.VECTOR_DIRECTION_TO);
		assertEquals(true, type.hasVectorDirectionTO());
	}
	
	/**
	 * Port is not a vector.
	 */
	@Test
	public void hasVectorDirectionTO2() {
		assertEquals(false, type.hasVectorDirectionTO());
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		System.out.println("********************");
		System.out.println("DefaultType testing...");
		System.out.println("Testing method toString():");
		System.out.println(type2);
		System.out.println("********************");
	}
	
}