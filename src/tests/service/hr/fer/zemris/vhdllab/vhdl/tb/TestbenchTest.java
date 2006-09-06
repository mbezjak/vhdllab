package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;

import java.text.ParseException;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.tb.Testbench} class.
 * 
 * @author Miro Bezjak
 */
public class TestbenchTest extends TestCase {

	public TestbenchTest(String name) {
		super(name);
	} 
	
	/** Test method writeVHDL(CircuitInterface, String) */
	public void testWriteVHDLCircuitInterfaceString() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		
		DefaultCircuitInterface ci = new DefaultCircuitInterface("sklop");
		ci.addPort(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("b", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("c", Direction.OUT, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("d", Direction.IN, new DefaultType("std_logic_vector", new int[] {0,0}, "TO")));
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {2,0}, "DOWNTO")));
		ci.addPort(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {1,4}, "TO")));
		
		String writted = null;
		try {
			writted = Testbench.writeVHDL(ci, inducement);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		System.out.println("Test method writeVHDL(CircuitInterface, String)...");
		System.out.println(inducement);
		System.out.println("********************************************\n");
		System.out.println(ci);
		System.out.println("********************************************\n");
		System.out.println(writted);
		System.out.println("..................................................");
	}
	
	/** Test method writeVHDL(CircuitInterface, String) when circuit interface is null */
	public void testWriteVHDLCircuitInterfaceString2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		
		try {
			Testbench.writeVHDL(null, inducement);
			fail("No exception when circuit interface is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method writeVHDL(CircuitInterface, String) when inducement is null */
	public void testWriteVHDLCircuitInterfaceString3() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("sklop");
		ci.addPort(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("b", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("c", Direction.OUT, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("d", Direction.IN, new DefaultType("std_logic_vector", new int[] {0,0}, "TO")));
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {2,0}, "DOWNTO")));
		ci.addPort(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {1,4}, "TO")));
		
		String inducement = null;
		try {
			Testbench.writeVHDL(ci, inducement);
			fail("No exception when inducement is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method writeVHDL(CircuitInterface, Generator) */
	public void testWriteVHDLCircuitInterfaceGenerator() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		DefaultGenerator gen = null;
		try {
			gen = new DefaultGenerator(inducement);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		DefaultCircuitInterface ci = new DefaultCircuitInterface("sklop");
		ci.addPort(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("b", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("c", Direction.OUT, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("d", Direction.IN, new DefaultType("std_logic_vector", new int[] {0,0}, "TO")));
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {2,0}, "DOWNTO")));
		ci.addPort(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {1,4}, "TO")));
		
		String writted = null;
		try {
			writted = Testbench.writeVHDL(ci, gen);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
		
		System.out.println("Test method writeVHDL(CircuitInterface, Generator)...");
		System.out.println(inducement);
		System.out.println("********************************************\n");
		System.out.println(gen);
		System.out.println("********************************************\n");
		System.out.println(ci);
		System.out.println("********************************************\n");
		System.out.println(writted);
		System.out.println("....................................................");
	}
	
	/** Test method writeVHDL(CircuitInterface, Generator) when circuit interface is null */
	public void testWriteVHDLCircuitInterfaceGenerator2() {
		String inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		
		try {
			Testbench.writeVHDL(null, inducement);
			fail("No exception when circuit interface is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/** Test method writeVHDL(CircuitInterface, Generator) when generator is null */
	public void testWriteVHDLCircuitInterfaceGenerator3() {
		DefaultCircuitInterface ci = new DefaultCircuitInterface("sklop");
		ci.addPort(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("b", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("c", Direction.OUT, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("d", Direction.IN, new DefaultType("std_logic_vector", new int[] {0,0}, "TO")));
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {2,0}, "DOWNTO")));
		ci.addPort(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {1,4}, "TO")));
		
		DefaultGenerator gen = null;
		try {
			Testbench.writeVHDL(ci, gen);
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
}