package hr.fer.zemris.vhdllab.vhdl.model;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.DefaultPort} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultPortTest extends TestCase {

	public DefaultPortTest(String name) {
		super(name);
	}

	/** Test constructor when name is null and port is a scalar*/
	public void testDefaultPort() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		try {
			new DefaultPort(null, Direction.IN, type);
			fail("No exception on when name is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when name is null and port is a vector*/
	public void testDefaultPort2() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		try {
			new DefaultPort(null, Direction.IN, type);
			fail("No exception on when name is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when direction is null and port is a scalar*/
	public void testDefaultPort3() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		try {
			new DefaultPort("a", null, type);
			fail("No exception on when direction is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when direction is null and port is a vector*/
	public void testDefaultPort4() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		try {
			new DefaultPort("a", null, type);
			fail("No exception on when direction is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}

	/** Test constructor when type is null*/
	public void testDefaultPort5() {
		try {
			new DefaultPort("a", Direction.IN, null);
			fail("No exception on when type is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when port is a scalar*/
	public void testDefaultPort6() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		try {
			new DefaultPort("a", Direction.IN, type);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when port is a vector*/
	public void testDefaultPort7() {
		DefaultType type = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		try {
			new DefaultPort("a", Direction.IN, type);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a scalar*/
	public void testEqualsAndHashCode() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		
		DefaultPort port1 = new DefaultPort("a", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.IN, type2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a vector*/
	public void testEqualsAndHashCode2() {
		DefaultType type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		
		DefaultPort port1 = new DefaultPort("a", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.IN, type2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and port is a scalar*/
	public void testEqualsAndHashCode3() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		
		DefaultPort port1 = new DefaultPort("a", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.OUT, type2);
		
		assertEquals(false, port1.equals(port2));
		assertNotSame(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false and port is a vector*/
	public void testEqualsAndHashCode4() {
		DefaultType type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		
		DefaultPort port1 = new DefaultPort("a", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.OUT, type2);
		
		assertEquals(false, port1.equals(port2));
		assertNotSame(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a scalar*/
	public void testEqualsAndHashCode5() {
		DefaultType type1 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultType type2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		
		DefaultPort port1 = new DefaultPort("A", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.IN, type2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return true and port is a vector*/
	public void testEqualsAndHashCode6() {
		DefaultType type1 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		DefaultType type2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		
		DefaultPort port1 = new DefaultPort("A", Direction.IN, type1);
		DefaultPort port2 = new DefaultPort("a", Direction.IN, type2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultPort port = new DefaultPort("a", Direction.IN, type);
		boolean val = port.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultPort */
	public void testEqualsObject2() {
		DefaultType type = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		DefaultPort port = new DefaultPort("a", Direction.IN, type);
		boolean val = port.equals(new String("a"));
		assertEquals(false, val);
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	public void testToString() {
		DefaultType type = new DefaultType("std_logic", new int[] {2, 0}, "DOWNTO");
		DefaultPort port = new DefaultPort("a", Direction.IN, type);
		System.out.println("********************");
		System.out.println("DefaultPort testing...");
		System.out.println("Testing method toString():");
		System.out.println(port);
		System.out.println("********************");
	}
}
