package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;

import java.text.ParseException;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestbenchTest {
	
	private static String inducement;
	private static DefaultCircuitInterface ci;
	private static Generator gen;
	
	@BeforeClass
	public static void init() throws ParseException {
		inducement = new String("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		gen = new DefaultGenerator(inducement);
		ci = new DefaultCircuitInterface("sklop");
		ci.addPort(new DefaultPort("a", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("b", Direction.IN, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("c", Direction.OUT, new DefaultType("std_logic", null, null)));
		ci.addPort(new DefaultPort("d", Direction.IN, new DefaultType("std_logic_vector", new int[] {0,0}, "TO")));
		ci.addPort(new DefaultPort("e", Direction.IN, new DefaultType("std_logic_vector", new int[] {2,0}, "DOWNTO")));
		ci.addPort(new DefaultPort("f", Direction.OUT, new DefaultType("std_logic_vector", new int[] {1,4}, "TO")));
	}
	
	@Test
	public void writeVHDLCircuitInterfaceString() throws ParseException, IncompatibleDataException {
		Testbench.writeVHDL(ci, inducement);
	}
	
	/**
	 * Circuit interface is <code>null</code>.
	 * @throws IncompatibleDataException 
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDLCircuitInterfaceString2() throws ParseException, IncompatibleDataException {
		Testbench.writeVHDL(null, inducement);
	}
	
	/**
	 * Inducement is <code>null</code>.
	 * @throws IncompatibleDataException 
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDLCircuitInterfaceString3() throws ParseException, IncompatibleDataException {
		String inducement = null;
		Testbench.writeVHDL(ci, inducement);
	}
	
	@Test
	public void writeVHDLCircuitInterfaceGenerator() throws ParseException, IncompatibleDataException {
		Testbench.writeVHDL(ci, gen);
	}
	
	/**
	 * Circuit interface is <code>null</code>.
	 * @throws IncompatibleDataException 
	 * @throws ParseException 
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDLCircuitInterfaceGenerator2() throws ParseException, IncompatibleDataException {
		Testbench.writeVHDL(null, inducement);
	}
	
	/**
	 * Generator is <code>null</code>.
	 * @throws ParseException
	 * @throws IncompatibleDataException
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDLCircuitInterfaceGenerator3() throws ParseException, IncompatibleDataException {
		DefaultGenerator gen = null;
		Testbench.writeVHDL(ci, gen);
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestbenchTest.class);
	}
	
}