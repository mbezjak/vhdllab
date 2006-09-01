package hr.fer.zemris.delab.tests;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.tb.DefaultGenerator;
import hr.fer.zemris.vhdllab.vhdl.tb.Generator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface}
 * class.
 * 
 * @author Miro Bezjak
 */
public class DefaultCircuitInterfaceTest extends TestCase {

	public DefaultCircuitInterfaceTest(String name) {
		super(name);
	}

	/** Test constructor when name is null */
	public void testDefaultCircuitInterfaceString() {
		try {
			new DefaultCircuitInterface(null);
			fail("No exception on when name is null");
		} catch(NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}

	/** Test constructor when name is not of correct format */
	public void testDefaultCircuitInterfaceString2() {
		try {
			new DefaultCircuitInterface("circuit_");
			fail("No exception on when name is not of correct format");
		} catch(IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when name is correct */
	public void testDefaultCircuitInterfaceString3() {
		try {
			new DefaultCircuitInterface("circuit_0");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when port list is null */
	public void testDefaultCircuitInterfaceStringListOfPort() {
		List<Port> ports = null;
		try {
			new DefaultCircuitInterface("circuit_0", ports);
			fail("No exception on when port list is null");
		} catch(NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when port list is correct */
	public void testDefaultCircuitInterfaceStringListOfPort2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci = null;
		try {
			ci = new DefaultCircuitInterface("circuit_0", ports);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		
		assertEquals(ci, new DefaultCircuitInterface("circuit_0", portList));
	}

	/** Test constructor when port is null */
	public void testDefaultCircuitInterfaceStringPort() {
		Port port = null;
		try {
			new DefaultCircuitInterface("circuit_0", port);
			fail("No exception on when port is null");
		} catch(NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test constructor when port is correct */
	public void testDefaultCircuitInterfaceStringPort2() {
		Port port = new DefaultPort("A", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));
		CircuitInterface ci = null;
		try {
			ci = new DefaultCircuitInterface("circuit_0", port);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		Port portCI = new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION));

		assertEquals(ci, new DefaultCircuitInterface("circuit_0", portCI));
	}

	/** Test method equals(Object) and hashCode() when equals return true */
	public void testEqualsAndHashCode() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci1 = new DefaultCircuitInterface("circuit_0", ports);
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);
		
		assertEquals(true, ci1.equals(ci2));
		assertEquals(ci1.hashCode(), ci2.hashCode());
	}
	
	/** Test method equals(Object) and hashCode() when equals return false */
	public void testEqualsAndHashCode2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci1 = new DefaultCircuitInterface("circuit_0", ports);
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);
		
		assertEquals(false, ci1.equals(ci2));
		assertNotSame(ci1.hashCode(), ci2.hashCode());
	}
	
	/** Test method equals(Object) if argument is null */
	public void testEqualsObject() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		boolean val = ci.equals(null);
		assertEquals(false, val);
	}
	
	/** Test method equals(Object) if argument is not instanceof DefaultCircuitInterface */
	public void testEqualsObject2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		boolean val = ci.equals(new String("circuit_0"));
		assertEquals(false, val);
	}
	
	/** Test method getPort(String) */
	public void testGetPort() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		Port port = new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO));
		
		assertEquals(port, ci.getPort("C"));
	}

	/** Test method getPorts(). Try to add a port from the outside */
	public void testGetPorts() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		try {
			ci.getPorts().add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
			fail("Outside modification possible");
		} catch (UnsupportedOperationException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}

	/** Test method isCompatible() when generator is null */
	public void testIsCompatible() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0");
		
		try {
			ci.isCompatible(null);
			fail("No exception on when generator is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method isCompatible() when generator is correct */
	public void testIsCompatible2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
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
		
		Generator generator = null;
		try {
			generator = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException while creating generator");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception other then ParseException while creating generator");
		}
		
		assertEquals(true, ci.isCompatible(generator));		
	}

	/** Test method addPort(Port) when port is null */
	public void testAddPort() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		try {
			ci.addPort(null);
			fail("No exception on when port is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addPort(Port) when port is correct */
	public void testAddPort2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		DefaultCircuitInterface ci1 = new DefaultCircuitInterface("circuit_0", ports);
		
		try {
			ci1.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}

		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		portList.add(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {5, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		DefaultCircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);

		assertEquals(true, ci1.equals(ci2));
	}

	/** Test method addPortList(ListOfPort) when port list is null */
	public void testAddPortList() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		DefaultCircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		
		try {
			ci.addPortList(null);
			fail("No exception on when port list is null");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
	}
	
	/** Test method addPortList(ListOfPort) when port list is correct */
	public void testAddPortList2() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		DefaultCircuitInterface ci1 = new DefaultCircuitInterface("circuit_0", ports);
		
		List<Port> portsAdd = new ArrayList<Port>();
		portsAdd.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portsAdd.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portsAdd.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portsAdd.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {8, 3}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		
		try {
			ci1.addPortList(portsAdd);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception generated: "+e.getMessage());
		}
		
		List<Port> portList = new ArrayList<Port>();
		portList.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		portList.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		portList.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		portList.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		DefaultCircuitInterface ci2 = new DefaultCircuitInterface("circuit_0", portList);

		assertEquals(true, ci1.equals(ci2));
	}

	/**
	 * Test method toString(). No asserting necessary,
	 * just testing to see this method work.
	 */
	public void testToString() {
		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("b", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("c", Direction.IN, new DefaultType("std_logic_vector", new int[] {2, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("D", Direction.OUT, new DefaultType("std_logic_vector", new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		ports.add(new DefaultPort("E", Direction.OUT, new DefaultType("std_logic", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {7, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		CircuitInterface ci = new DefaultCircuitInterface("circuit_0", ports);
		System.out.println("********************");
		System.out.println("DefaultCircuitInterface testing...");
		System.out.println("Testing method toString():");
		System.out.println(ci);
		System.out.println("********************");
	}
}
