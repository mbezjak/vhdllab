package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.utilities.SerializationUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link DefaultCircuitInterface} class.
 * 
 * @author Miro Bezjak
 */
public class DefaultCircuitInterfaceTest {
	
	private static final String NAME = "circuit_0";
	private List<Port> ports;
	private CircuitInterface ci;
	
	/**
	 * Before every test.
	 */
	@Before
	public void init() {
		ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		ci = new DefaultCircuitInterface("Circuit_0", ports);
	}
	
	/**
	 * Name is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor() {
		new DefaultCircuitInterface(null);
	}

	/**
	 * Name is not of correct format (ends with an underscore).
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructor2() {
		new DefaultCircuitInterface("circuit_");
	}
	
	/**
	 * Name is of correct format.
	 */
	@Test
	public void constructor3() {
		new DefaultCircuitInterface(NAME);
	}
	
	/**
	 * Port list is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor4() {
		new DefaultCircuitInterface(NAME, (List<Port>)null);
	}
	
	/**
	 * Port list is correct.
	 */
	@Test
	public void constructor5() {
		new DefaultCircuitInterface(NAME, ports);
	}
	
	/**
	 * Port list contains two ports with same name, just different case.
	 */
	@Test
	public void constructor6() {
		List<Port> portList = new ArrayList<Port>(ports);
		portList.add(new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		CircuitInterface ci = new DefaultCircuitInterface(NAME, ports);
		
		assertEquals(ci, new DefaultCircuitInterface(NAME, portList));
	}

	/**
	 * Port is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructor7() {
		new DefaultCircuitInterface(NAME, (Port)null);
	}
	
	/**
	 * Circuit interfaces are equal.
	 */
	@Test
	public void equalsAndHashCode() {
		List<Port> portList = new ArrayList<Port>(ports);
		CircuitInterface ci2 = new DefaultCircuitInterface(NAME, portList);
		
		assertEquals(true, ci.equals(ci2));
		assertEquals(ci.hashCode(), ci2.hashCode());
	}
	
	/**
	 * Circuit interfaces are not equal.
	 */
	@Test
	public void equalsAndHashCode2() {
		List<Port> portList = new ArrayList<Port>(ports);
		portList.remove(0);
		CircuitInterface ci2 = new DefaultCircuitInterface(NAME, portList);
		
		assertEquals(false, ci.equals(ci2));
		assertNotSame(ci.hashCode(), ci2.hashCode());
	}
	
	/**
	 * Test if two circuit interfaces with ports names "A" and "a" are equal.
	 */
	@Test
	public void equalsAndHashCode3() {
		Port port = new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));
		CircuitInterface ci = new DefaultCircuitInterface(NAME, port);
		
		Port portCI = new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));
		CircuitInterface ci2 = new DefaultCircuitInterface(NAME, portCI);
		assertEquals(ci, ci2);
		assertEquals(ci.hashCode(), ci2.hashCode());
	}
	
	/**
	 * Object is <code>null</code>.
	 */
	@Test
	public void equalsObject() {
		assertEquals(false, ci.equals(null));
	}
	
	/**
	 * Object is not instance of <code>CircuitInerface</code>.
	 */
	@Test
	public void equalsObject2() {
		assertEquals(false, ci.equals(new String(NAME)));
	}
	
	/**
	 * Test getPort method.
	 */
	@Test
	public void getPort() {
		Port port = new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO));
		assertEquals(port, ci.getPort("C"));
	}

	/**
	 * Try to add a port after method getPorts().
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void getPorts() {
		ci.getPorts().add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization() {
		CircuitInterface deserializedCi = (CircuitInterface) SerializationUtil
				.serializeThenDeserializeObject(ci);
		assertEquals(ci, deserializedCi);
		assertEquals(ci.hashCode(), deserializedCi.hashCode());
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	@Ignore("Writting on screen... Already tested!")
	@Test
	public void toStringTest() {
		ports.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		CircuitInterface ci = new DefaultCircuitInterface(NAME, ports);
		System.out.println("********************");
		System.out.println("DefaultCircuitInterface testing...");
		System.out.println("Testing method toString():");
		System.out.println(ci);
		System.out.println("********************");
	}
	
}