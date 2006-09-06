package hr.fer.zemris.vhdllab.vhdl.model;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.DefaultType} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultTypeTest extends TestCase {

	public DefaultTypeTest(String name) {
		super(name);
	}
	
	/** Test constructor when name is null */
	public void testDefaultType() {
		try {
			new DefaultType(null, DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
			fail("No exception on when name is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when name of wrong format */
	public void testDefaultType2() {
		try {
			new DefaultType("1std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
			fail("No exception on when name is of wrong format");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when range is null and vector direction isnt */
	public void testDefaultType3() {
		try {
			new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.VECTOR_DIRECTION_DOWNTO);
			fail("No exception on when range is null and vector direction isnt");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when vector direction is null and range isnt */
	public void testDefaultType4() {
		try {
			new DefaultType("std_logic", new int[] {2, 0}, DefaultType.SCALAR_VECTOR_DIRECTION);
			fail("No exception on when vector direction is null and range isnt");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when range contains more then two elements */
	public void testDefaultType5() {
		try {
			new DefaultType("std_logic_vector", new int[] {2, 0, 1}, DefaultType.VECTOR_DIRECTION_DOWNTO);
			fail("No exception on when range contains more then two elements");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when range contains at least one negative element */
	public void testDefaultType6() {
		try {
			new DefaultType("std_logic_vector", new int[] {-2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
			fail("No exception on when range contains at least one negative element");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when vector direction is incorrect */
	public void testDefaultType7() {
		try {
			new DefaultType("std_logic_vector", new int[] {2, 0}, "UPTO");
			fail("No exception on when vector direction is incorrect");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when range elements are not consistent with vector direction */
	public void testDefaultType8() {
		try {
			new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_TO);
			fail("No exception on when range elements are not consistent with vector direction");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when range elements are not consistent with vector direction */
	public void testDefaultType9() {
		try {
			new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO);
			fail("No exception on when range elements are not consistent with vector direction");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when declaring a vector with bounds 0, 0 */
	public void testDefaultType10() {
		try {
			new DefaultType("std_logic_vector", new int[] {0, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when declaring a normal vector */
	public void testDefaultType11() {
		try {
			new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}

	/** Test method equals(Object) and hashCode() when equals return true and port is a scalar*/
	public void testEqualsAndHashCode() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a vector*/
	public void testEqualsAndHashCode2() {
		DefaultType type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and port is a scalar*/
	public void testEqualsAndHashCode3() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("std_logic2", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(false, type1.equals(type2));
		assertNotSame(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and port is a vector*/
	public void testEqualsAndHashCode4() {
		DefaultType type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("std_logic_vector2", new int[] {0, 2}, DefaultType.VECTOR_DIRECTION_TO);
		assertEquals(false, type1.equals(type2));
		assertNotSame(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a scalar*/
	public void testEqualsAndHashCode5() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("STD_LOGIC", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a vector*/
	public void testEqualsAndHashCode6() {
		DefaultType type1 = new DefaultType("std_logic", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("STD_LOGIC", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type1.equals(type2));
		assertEquals(type1.hashCode(), type2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		boolean val = type.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultType */
	public void testEqualsObject2() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		boolean val = type.equals(new String("std_logic"));
		assertEquals(false, val);
	}
	
	/** Test method getRangeFrom() when port is a scalar */
	public void testGetRangeFrom() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		try {
			type.getRangeFrom();
			fail("No exception on method getRangeFrom when port is a scalar");
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method getRangeFrom() when port is a vector */
	public void testGetRangeFrom2() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		int ret = -1;
		try {
			ret = type.getRangeFrom();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		assertEquals(2, ret);
	}

	/** Test method getRangeTo() when port is a scalar */
	public void testGetRangeTo() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		try {
			type.getRangeTo();
			fail("No exception on method getRangeFrom when port is a scalar");
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method getRangeTo() when port is a vector */
	public void testGetRangeTo2() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		int ret = -1;
		try {
			ret = type.getRangeTo();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		assertEquals(0, ret);
	}

	/** Test method isScalar() when port is a scalar */
	public void testIsScalar() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(true, type.isScalar());
	}
	
	/** Test method isScalar() when port is not a scalar */
	public void testIsScalar2() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(false, type.isScalar());
	}

	/** Test method isVector() when port is a vector */
	public void testIsVector() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type.isVector());
	}
	
	/** Test method isVector() when port is not a vector */
	public void testIsVector2() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(false, type.isVector());
	}

	/** Test method hasVectorDirectionDOWNTO() when port is a vector */
	public void testHasVectorDirectionDOWNTO() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		assertEquals(true, type.hasVectorDirectionDOWNTO());
	}
	
	/** Test method hasVectorDirectionDOWNTO() when port is not a vector */
	public void testHasVectorDirectionDOWNTO2() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(false, type.hasVectorDirectionDOWNTO());
	}

	/** Test method hasVectorDirectionTO() when port is a vector */
	public void testHasVectorDirectionTO() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO);
		assertEquals(true, type.hasVectorDirectionTO());
	}
	
	/** Test method hasVectorDirectionTO() when port is not a vector */
	public void testHasVectorDirectionTO2() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		assertEquals(false, type.hasVectorDirectionTO());
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	public void testToString() {
		DefaultType type = new DefaultType("std_logic", new int[] {2, 0}, "DOWNTO");
		System.out.println("********************");
		System.out.println("DefaultType testing...");
		System.out.println("Testing method toString():");
		System.out.println(type);
		System.out.println("********************");
	}
}
