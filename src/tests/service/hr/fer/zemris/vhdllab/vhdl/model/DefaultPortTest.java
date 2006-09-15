package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultPortTest {
	
	private static Type typeS;
	private static Type typeS2;
	private static Type typeV;
	private static Type typeV2;
	
	@BeforeClass
	public static void init() {
		typeS = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		typeS2 = new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
		typeV = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
		typeV2 = new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO);
	}
	
	/**
	 * Name is <code>null</code> and port is a scalar.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor() {
		new DefaultPort(null, Direction.IN, typeS);
	}
	
	/**
	 * Name is <code>null</code> and port is a vector.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor2() {
		new DefaultPort(null, Direction.IN, typeV);
	}
	
	/**
	 * Direction is <code>null</code> and port is a scalar.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor3() {
		new DefaultPort("a", null, typeS);
	}
	
	/**
	 * Direction is <code>null</code> and port is a vector.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor4() {
		new DefaultPort("a", null, typeV);
	}

	/**
	 * Type is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor5() {
		new DefaultPort("a", Direction.IN, null);
	}
	
	/**
	 * Port is a scalar.
	 */
	@Test
	public void constructor6() {
		new DefaultPort("a", Direction.IN, typeS);
	}
	
	/**
	 * Port is a vector.
	 */
	@Test
	public void constructor7() {
		new DefaultPort("a", Direction.IN, typeV);
	}
	
	/**
	 * Ports are equal and they are scalar.
	 */
	@Test
	public void equalsAndHashCode() {
		Port port1 = new DefaultPort("a", Direction.IN, typeS);
		Port port2 = new DefaultPort("a", Direction.IN, typeS2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are equal and they are vector.
	 */
	@Test
	public void equalsAndHashCode2() {
		Port port1 = new DefaultPort("a", Direction.IN, typeV);
		Port port2 = new DefaultPort("a", Direction.IN, typeV2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are not equal and they are scalar.
	 */
	@Test
	public void equalsAndHashCode3() {
		Port port1 = new DefaultPort("a", Direction.IN, typeS);
		Port port2 = new DefaultPort("a", Direction.OUT, typeS2);
		
		assertEquals(false, port1.equals(port2));
		assertNotSame(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are not equal and they are vector.
	 */
	@Test
	public void equalsAndHashCode4() {
		Port port1 = new DefaultPort("a", Direction.IN, typeV);
		Port port2 = new DefaultPort("a", Direction.OUT, typeV2);
		
		assertEquals(false, port1.equals(port2));
		assertNotSame(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are equal and they are scalar.
	 */
	@Test
	public void equalsAndHashCode5() {
		Port port1 = new DefaultPort("A", Direction.IN, typeS);
		Port port2 = new DefaultPort("a", Direction.IN, typeS2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are equal and they are vector.
	 */
	@Test
	public void equalsAndHashCode6() {
		Port port1 = new DefaultPort("A", Direction.IN, typeV);
		Port port2 = new DefaultPort("a", Direction.IN, typeV2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		Port port = new DefaultPort("a", Direction.IN, typeS);
		assertEquals(false, port.equals(null));
	}
	
	/**
	 * Object is not instance of Port.
	 */
	@Test
	public void equalsObject2() {
		Port port = new DefaultPort("a", Direction.IN, typeS);
		assertEquals(false, port.equals(new String("a")));
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		Port port = new DefaultPort("a", Direction.IN, typeV);
		System.out.println("********************");
		System.out.println("DefaultPort testing...");
		System.out.println("Testing method toString():");
		System.out.println(port);
		System.out.println("********************");
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DefaultPortTest.class);
	}
}
