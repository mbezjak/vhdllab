package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;

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
	
	@Test()
	public void writeVHDLCircuitInterfaceGenerator4() throws ParseException, IncompatibleDataException {
		String file1 = "library IEEE;" + "\n" +
		"use IEEE.STD_LOGIC_1164.ALL;" + "\n" + 
		"use IEEE.STD_LOGIC_ARITH.ALL;" + "\n" +
		"use IEEE.STD_LOGIC_UNSIGNED.ALL;" + "\n" +
		"\n" +
		"entity mux41 is" + "\n" +
		"port ( e :in std_logic;" + "\n" +
		"d:in std_logic_vector (3 downto 0);" + "\n" +
		"sel :in std_logic_vector (1 downto 0);" + "\n" +
		"z :out std_logic);" + "\n" +
		//");" + "\n" +
		"end mux41;" + "\n" +
		"" + "\n" +
		"architecture Behavioral of mux41 is" + "\n" +
		"" + "\n" +
		"begin" + "\n" +
		"process(d,e,sel)" + "\n" +
		"begin" + "\n" +
		"if (e = '1')then" + "\n" + 
		"case sel is" + "\n" +
		"when  \"00\" => z <= d(0);" + "\n" +
		"when  \"01\" => z <= d(1);" + "\n" +
		"when  \"10\" => z <= d(2);" + "\n" +
		"when  \"11\" => z <= d(3);" + "\n" +
		"when others => z <='0';" + "\n" +
		"end case;" + "\n" +
		"else z<='0';" + "\n" +
		"end if;" + "\n" +
		"end process;" + "\n" +
		"end Behavioral";
		
		CircuitInterface ci = Extractor.extractCircuitInterface(file1);
		
		String file2 = "<file>File2</file>" + "\n" + 
						"<measureUnit>ns</measureUnit>" + "\n" +
						"<duration>700</duration>" + "\n" +
						"<signal name=\"E\" type=\"scalar\">(0,1)</signal>" + "\n" + 
						"<signal name=\"D\" type=\"vector\" rangeFrom=\"3\" rangeTo=\"0\">(0,0000)(20,1000)(30,1100)(40,1110)(50,1111)(60,0111)(65,0101)(70,0001)(90,0000)(95,0010)(120,1110)(135,1100)(150,0100)(155,0001)(180,0000)(190,1010)(230,0010)(235,0110)(245,0100)(255,0101)(265,1101)(295,1001)(315,1000)(325,1010)(330,0010)(340,0110)(360,1111)(375,1101)(380,1001)(385,1000)</signal>" + "\n" + 
						"<signal name=\"SEL\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"0\">(0,00)(25,10)(35,11)(70,10)(85,00)(130,01)(165,11)(195,10)(200,00)(250,01)(260,00)(285,10)(310,11)(320,10)(350,00)(360,01)(385,00)(395,10)(410,00)(415,01)(430,00)</signal>" + "\n";
		
		Generator gen = new DefaultGenerator(file2);

		Testbench.writeVHDL(ci, gen);
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestbenchTest.class);
	}
	
}