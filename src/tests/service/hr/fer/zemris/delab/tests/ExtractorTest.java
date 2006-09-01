package hr.fer.zemris.delab.tests;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.vhdllab.vhdl.model.Extractor} class.
 *
 * @author marcupic
 */
public class ExtractorTest extends TestCase {

	/**
	 * Test decommenting of VHDL source.
	 */
	public void testDecomment() {
		String primjer = "-- najava biblioteke\n" +
			"library ieee;\n" +
			"--najava paketa\n" +
			"use ieee.std_logic_1164.all;\n" +
			"-- zavrsni komentar";
		String tocno = "library ieee;\n" +
			"use ieee.std_logic_1164.all;\n";
		String result = null;

		try {
			result = Extractor.decomment(primjer);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		assertEquals(tocno, result);
	}

	/**
	 * Test decommenting of VHDL source, when source ends with
	 * an empty comment.
	 */
	public void testDecomment2() {
		String primjer = "-- najava biblioteke\n" +
			"library ieee;\n" +
			"--najava paketa\n" +
			"use ieee.std_logic_1164.all;\n" +
			"-- zavrsni komentar\n" +
			"nesto --";
		String tocno = "library ieee;\n" +
			"use ieee.std_logic_1164.all;\n" +
			"nesto ";
		String result = null;

		try {
			result = Extractor.decomment(primjer);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		assertEquals(tocno, result);
	}

	/**
	 * Test removal of whitespaces.
	 */
	public void testRemoveWhiteSpaces() {
		String primjer = "  \n" +
			" library   ieee;\n" +
			"use  ieee.std_logic_1164.all;\n" +
			"   ";
		String tocno = "library ieee; use ieee.std_logic_1164.all;";
		String result = null;

		try {
			result = Extractor.removeWhiteSpaces(primjer);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		assertEquals(tocno, result);
	}

	/**
	 * Test removal of whitespaces.
	 */
	public void testRemoveWhiteSpaces2() {
		String primjer = "  \n" +
			" library   ieee;\n" +
			"use  ieee.std_logic_1164.all;\n" +
			"   ;";
		String tocno = "library ieee; use ieee.std_logic_1164.all; ;";
		String result = null;

		try {
			result = Extractor.removeWhiteSpaces(primjer);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		assertEquals(tocno, result);
	}

	/**
	 * Test interface extraction.
	 */
	public void testExtractCircuitInterface() {
		String source = "-- najava biblioteke\n" +
			"library ieee;\n" +
			"--najava paketa\n" +
			"use ieee.std_logic_1164.all;\n" +
			"-- zavrsni komentar\n" +
			"nesto --\n"+
			"Entity sklop_0 is \n"+
			"  generic (n, pa : natural := 27; zet:std_logic_vector(3 downto 1):= \"0111\");\n"+
			"  port (a, b, z : IN std_logic := '0'; q: OUT std_logic_vector(3 downto 0); w: std_logic_vector(0 to 2) := \"101\");\n"+
			"end Entity sklop_0\n";
		CircuitInterface ci = null;

		try {
			ci = Extractor.extractCircuitInterface(source);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}

		List<Port> ports = new ArrayList<Port>();
		ports.add(new DefaultPort("A", Direction.IN, new DefaultType("STD_LOGIC", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("B", Direction.IN, new DefaultType("STD_LOGIC", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("Z", Direction.IN, new DefaultType("STD_LOGIC", DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		ports.add(new DefaultPort("Q", Direction.OUT, new DefaultType("STD_LOGIC_VECTOR", new int[] {3, 0}, DefaultType.VECTOR_DIRECTION_DOWNTO)));
		ports.add(new DefaultPort("W", Direction.IN, new DefaultType("STD_LOGIC_VECTOR", new int[] {0, 2}, DefaultType.VECTOR_DIRECTION_TO)));

		assertEquals(new DefaultCircuitInterface("sklop_0", ports), ci);
	}
	
	/**
	 * Test interface extraction, when interface is empty.
	 */
	public void testExtractCircuitInterfaceWhenEmpty() {
		String source = "-- najava biblioteke\n" +
			"library ieee;\n" +
			"--najava paketa\n" +
			"use ieee.std_logic_1164.all;\n" +
			"-- zavrsni komentar\n" +
			"nesto --\n"+
			"Entity sklop_0 is \n"+
			"end Entity sklop_0\n";
		CircuitInterface ci = null;
		
		try {
			ci = Extractor.extractCircuitInterface(source);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		assertEquals(new DefaultCircuitInterface("sklop_0"), ci);
	}

	public void testExtractUsedComponents() {
		String primjer = "-- najava biblioteke\n" +
		"library ieee;\n" +
		"--najava paketa\n" +
		"use ieee.std_logic_1164.all;\n" +
		"-- zavrsni komentar\n" +
		"nesto --\n"+
		"Entity sklop_0 is \n"+
		"end Entity sklop_0\n"+
		"architecture strukturna of sklop_0 is\n"+
		"component sklopI port (); end component;\n"+
		"component sklopILI port (); end component;\n"+
		"begin\n"+
		"c0: sklopI port map ();\n"+
		"c1: sklopILI port map ();\n"+
		"c2: ENTITY work.sklopNE port map ();\n"+
		"end strukturna;\n";
		
		Set<String> result = null;
		try {
			result = Extractor.extractUsedComponents(primjer);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		Set<String> tocno = new HashSet<String>();
		tocno.add("SKLOPI");
		tocno.add("SKLOPILI");
		tocno.add("SKLOPNE");
		assertEquals(tocno,result);
	}
	
	public void testExtractHierarchy() {
		List<String> identifiers = new ArrayList<String>();
		identifiers.add("0");
		identifiers.add("1");
		identifiers.add("2");
		identifiers.add("3");
		identifiers.add("4");
		
		try {
			Extractor.extractHierarchy(identifiers, provider);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("Exception generated: "+ex.getMessage());
		}
		
		fail("Not yet implemented.");
	}
	
	Extractor.VHDLSourceProvider provider = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new Extractor.VHDLSourceProvider() {
		
			public String provide(String identifier) throws Exception {
				int n = Integer.parseInt(identifier);
				
				switch(n) {
				case 0:
					return
				"library ieee;\n"+
				"use ieee.std_logic_1164.all;\n"+
				"\n"+
				"entity sklopI is port (\n"+
				"  a, b: in std_logic;\n"+
				"  y: out std_logic\n"+
				");\n"+
				"end sklopI;\n"+
				"\n"+
				"architecture ponasajna of sklopI is\n"+
				"begin\n"+
				"  y <= a and b;\n"+
				"end ponasajna;\n";

				case 1:
					return "library ieee;\n"+
				"use ieee.std_logic_1164.all;\n"+
				"\n"+
				"entity sklopNE is port (\n"+
				"  a: in std_logic;\n"+
				"  y: out std_logic\n"+
				");\n"+
				"end sklopNE;\n"+
				"\n"+
				"architecture ponasajna of sklopNE is\n"+
				"begin\n"+
				"  y <= not a;\n"+
				"end ponasajna;\n";

				case 2:
					return "library ieee;\n"+
				"use ieee.std_logic_1164.all;\n"+
				"\n"+
				"entity sklopNI is port (\n"+
				"  a, b: in std_logic;\n"+
				"  y: out std_logic\n"+
				");\n"+
				"end sklopNI;\n"+
				"\n"+
				"architecture ponasajna of sklopNI is\n"+
				" signal tmp: std_logic;\n"+
				"begin\n"+
				"  c0: ENTITY work.sklopI port map (a,b,tmp);\n"+
				"  c1: ENTITY work.sklopNE port map (tmp,y);\n"+
				"end ponasajna;\n";

				case 3:
					return "library ieee;\n"+
				"use ieee.std_logic_1164.all;\n"+
				"\n"+
				"entity sklopNINI is port (\n"+
				"  a, b, c: in std_logic;\n"+
				"  y: out std_logic\n"+
				");\n"+
				"end sklopNINI;\n"+
				"\n"+
				"architecture ponasajna of sklopNINI is\n"+
				" signal tmp: std_logic;\n"+
				"begin\n"+
				"  c0: ENTITY work.sklopNI port map (a,b,tmp);\n"+
				"  c1: ENTITY work.sklopNI port map (tmp,c,y);\n"+
				"end ponasajna;\n";


				case 4:
					return "library ieee;\n"+
				"use ieee.std_logic_1164.all;\n"+
				"\n"+
				"entity sklopNI_tb is \n"+
				"end sklopNI_tb;\n"+
				"\n"+
				"architecture ponasajna of sklopNI_tb is\n"+
				" signal a,b,y: std_logic;\n"+
				"begin\n"+
				"  uut: ENTITY work.sklopNI port map (a,b,y);\n"+
				"\n"+
				"  process\n"+
				"  begin\n"+
				"    a <= '0'; b <= '0';\n"+
				"    wait for 100 ns;\n"+
				"\n"+
				"    wait;\n"+
				"  end process;\n"+
				"\n"+
				"end ponasajna;\n";
					default:
						throw new IndexOutOfBoundsException("Illegal identifier!");
				}
			}
		};
	}
	
}
