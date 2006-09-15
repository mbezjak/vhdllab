package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.vhdl.tb.DefaultGenerator;
import hr.fer.zemris.vhdllab.vhdl.tb.Generator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultCircuitInterfaceTest {
	
	private List<Port> ports;
	private CircuitInterface ci;
	
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
	public void constructorString() {
		new DefaultCircuitInterface(null);
	}

	/**
	 * Name is not of correct format.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void constructorString2() {
		new DefaultCircuitInterface("circuit_");
	}
	
	/**
	 * Name is of correct format.
	 */
	@Test
	public void constructorString3() {
		new DefaultCircuitInterface("circuit_0");
	}
	
	/**
	 * Port list is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorStringListOfPort() {
		List<Port> ports = null;
		new DefaultCircuitInterface("circuit_0", ports);
	}
	
	/**
	 * Port list is correct.
	 */
	@Test
	public void constructorStringListOfPort2() {
		new DefaultCircuitInterface("circuit_0", ports);
	}
	
	/**
	 * Port list is correct.
	 */
	@Test
	public void constructorStringListOfPort3() {
		ports.add(new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		
		assertEquals(ci, new DefaultCircuitInterface("circuit_0", portList));
	}

	/**
	 * Port is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorStringPort() {
		Port port = null;
		new DefaultCircuitInterface("circuit_0", port);
	}
	
	/**
	 * Port is correct.
	 */
	@Test
	public void constructorStringPort2() {
		Port port = new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", port);
		
		Port portCI = new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));
		assertEquals(ci, new DefaultCircuitInterface("circuit_0", portCI));
	}

	/**
	 * Circuit interfaces are equal.
	 */
	@Test
	public void equalsAndHashCode() {
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);
		
		assertEquals(true, ci.equals(ci2));
		assertEquals(ci.hashCode(), ci2.hashCode());
	}
	
	/**
	 * Circuit interfaces are not equal.
	 */
	@Test
	public void equalsAndHashCode2() {
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		CircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);
		
		assertEquals(false, ci.equals(ci2));
		assertNotSame(ci.hashCode(), ci2.hashCode());
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
		assertEquals(false, ci.equals(new String("circuit_0")));
	}
	
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
	 * Generator is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void isCompatible() {
		ci.isCompatible(null);
	}
	
	/**
	 * Generator is correct
	 * @throws ParseException
	 */
	@Test
	public void isCompatible2() throws ParseException {
		ports.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		String inducement = new String("<measureUnit>ns</measureUnit>\n" + 
				"<duration>1000</duration>" + 
				"<signal name=\"a\" type=\"scalar\">(0, 0)(100, Z)(500, U)(600, 1)(700, 0)</signal>" + 
				"<signal name=\"B\" type=\"scalar\">(0, 1)(100, Z)(400, U)(600, 0)(700, 1)</signal>" + 
				"<signal name=\"c\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0, 000)(100, Z00)(500, U01)(550, 111)(700, 100)</signal>" + 
				"<signal name=\"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"3\">(0, 0010)(1010, Z000)(500, U011)(550, 11z1)(700, 1010)</signal>" + 
				"<signal name=\"e\" type=\"scalar\">(0, 1)(100, 1)(200, 0)(350, 1)(500, z)</signal>" + 
				"<signal name=\"F\" type=\"vector\" rangeFrom=\"7\" rangeTo=\"0\">(0, 0010110)(1010, Z001100)(500, U0zz011)(550, 11011z1)(700, 1011010)</signal>");
		Generator generator = new DefaultGenerator(inducement);
		assertEquals(true, ci.isCompatible(generator));		
	}

	/**
	 * Port is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void addPort() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		ci.addPort(null);
	}
	
	/**
	 * Port is correct.
	 */
	@Test
	public void addPort2() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));

		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		portList.add(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		DefaultCircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);

		assertEquals(true, ci.equals(ci2));
	}

	/**
	 * Port list is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void addPortList() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		ci.addPortList(null);
	}
	
	/**
	 * Port list is correct.
	 */
	@Test
	public void addPortList2() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		List<Port> portsAdd = new ArrayList<Port>();
		portsAdd.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portsAdd.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portsAdd.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portsAdd.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {8, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		
		ci.addPortList(portsAdd);
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		portList.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		DefaultCircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);

		assertEquals(true, ci.equals(ci2));
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
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		System.out.println("********************");
		System.out.println("DefaultCircuitInterface testing...");
		System.out.println("Testing method toString():");
		System.out.println(ci);
		System.out.println("********************");
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DefaultCircuitInterfaceTest.class);
	}
	
}