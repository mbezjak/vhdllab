package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.utilities.SerializationUtil;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link DefaultPort} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultPortTest {
	
	private static final String NAME = "port_A0";
	private static Type typeS;
	private static Type typeS2;
	private static Type typeV;
	private static Type typeV2;
	
	/**
	 * Initialize once.
	 */
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
		new DefaultPort(NAME, null, typeS);
	}
	
	/**
	 * Direction is <code>null</code> and port is a vector.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor4() {
		new DefaultPort(NAME, null, typeV);
	}

	/**
	 * Type is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor5() {
		new DefaultPort(NAME, Direction.IN, null);
	}
	
	/**
	 * Port is a scalar.
	 */
	@Test
	public void constructor6() {
		new DefaultPort(NAME, Direction.IN, typeS);
	}
	
	/**
	 * Port is a vector.
	 */
	@Test
	public void constructor7() {
		new DefaultPort(NAME, Direction.IN, typeV);
	}
	
	/**
	 * Ports are equal and they are scalar.
	 */
	@Test
	public void equalsAndHashCode() {
		Port port1 = new DefaultPort(NAME, Direction.IN, typeS);
		Port port2 = new DefaultPort(NAME, Direction.IN, typeS2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are equal and they are vector.
	 */
	@Test
	public void equalsAndHashCode2() {
		Port port1 = new DefaultPort(NAME, Direction.IN, typeV);
		Port port2 = new DefaultPort(NAME, Direction.IN, typeV2);
		
		assertEquals(true, port1.equals(port2));
		assertEquals(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are not equal and they are scalar.
	 */
	@Test
	public void equalsAndHashCode3() {
		Port port1 = new DefaultPort(NAME, Direction.IN, typeS);
		Port port2 = new DefaultPort(NAME, Direction.OUT, typeS2);
		
		assertEquals(false, port1.equals(port2));
		assertNotSame(port1.hashCode(), port2.hashCode());
	}
	
	/**
	 * Ports are not equal and they are vector.
	 */
	@Test
	public void equalsAndHashCode4() {
		Port port1 = new DefaultPort(NAME, Direction.IN, typeV);
		Port port2 = new DefaultPort(NAME, Direction.OUT, typeV2);
		
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
		Port port = new DefaultPort(NAME, Direction.IN, typeS);
		assertEquals(false, port.equals(null));
	}
	
	/**
	 * Object is not instance of Port.
	 */
	@Test
	public void equalsObject2() {
		Port port = new DefaultPort(NAME, Direction.IN, typeS);
		assertEquals(false, port.equals(new String(NAME)));
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization() {
		Port port = new DefaultPort(NAME, Direction.IN, typeS);
		Port port2 = (Port) SerializationUtil.serializeThenDeserializeObject(port);
		
		assertEquals(port, port2);
		assertEquals(port.hashCode(), port2.hashCode());
	}
	
	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		Port port = new DefaultPort(NAME, Direction.IN, typeV);
		System.out.println("********************");
		System.out.println("DefaultPort testing...");
		System.out.println("Testing method toString():");
		System.out.println(port);
		System.out.println("********************");
	}
}
