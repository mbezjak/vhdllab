package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.dummy.FileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.GlobalFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.ProjectDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.UserFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.impl.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimulateTest {

	@Test
	public void simulateTest() throws Exception {
		VHDLLabManagerImpl vhdlLabman = new VHDLLabManagerImpl();
		vhdlLabman.setFileDAO(new FileDAOMemoryImpl());
		vhdlLabman.setGlobalFileDAO(new GlobalFileDAOMemoryImpl());
		vhdlLabman.setProjectDAO(new ProjectDAOMemoryImpl(new FileDAOMemoryImpl()));
		vhdlLabman.setUserFileDAO(new UserFileDAOMemoryImpl());
		
		Project proj = vhdlLabman.createNewProject("testProject", "user1");
		File sklopFja = vhdlLabman.createNewFile(proj,"fja_a",FileTypes.FT_VHDL_SOURCE);
		File sklopFjaTb = vhdlLabman.createNewFile(proj,"fja_a_tb",FileTypes.FT_VHDL_SOURCE);
		sklopFja.setContent(provider.provide("0"));
		vhdlLabman.saveFile(sklopFja.getId(), sklopFja.getContent());
		sklopFjaTb.setContent(provider.provide("1"));
		vhdlLabman.saveFile(sklopFjaTb.getId(), sklopFjaTb.getContent());

		SimulationResult simRes = vhdlLabman.runSimulation(sklopFjaTb.getId());
		System.out.println("SimRes: "+simRes);
	}
	
	@Test
	public void simulateTest2() throws Exception {
		VHDLLabManagerImpl vhdlLabman = new VHDLLabManagerImpl();
		vhdlLabman.setFileDAO(new FileDAOMemoryImpl());
		vhdlLabman.setGlobalFileDAO(new GlobalFileDAOMemoryImpl());
		vhdlLabman.setProjectDAO(new ProjectDAOMemoryImpl(new FileDAOMemoryImpl()));
		vhdlLabman.setUserFileDAO(new UserFileDAOMemoryImpl());
		
		Project proj = vhdlLabman.createNewProject("testProject", "user1");
		File sklopFja = vhdlLabman.createNewFile(proj,"mux41",FileTypes.FT_VHDL_SOURCE);
		File sklopFjaTb = vhdlLabman.createNewFile(proj,"mux41_tb",FileTypes.FT_VHDL_TB);
		sklopFja.setContent("library IEEE;" + "\n" +
				"use IEEE.STD_LOGIC_1164.ALL;" + "\n" + 
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
				"end Behavioral;");
		vhdlLabman.saveFile(sklopFja.getId(), sklopFja.getContent());
		sklopFjaTb.setContent("<file>mux41</file>" + "\n" + 
				"<measureUnit>ns</measureUnit>" + "\n" +
				"<duration>700</duration>" + "\n" +
				"<signal name=\"E\" type=\"scalar\">(0,1)</signal>" + "\n" + 
				"<signal name=\"D\" type=\"vector\" rangeFrom=\"3\" rangeTo=\"0\">(0,0000)(20,1000)(30,1100)(40,1110)(50,1111)(60,0111)(65,0101)(70,0001)(90,0000)(95,0010)(120,1110)(135,1100)(150,0100)(155,0001)(180,0000)(190,1010)(230,0010)(235,0110)(245,0100)(255,0101)(265,1101)(295,1001)(315,1000)(325,1010)(330,0010)(340,0110)(360,1111)(375,1101)(380,1001)(385,1000)</signal>" + "\n" + 
				"<signal name=\"SEL\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"0\">(0,00)(25,10)(35,11)(70,10)(85,00)(130,01)(165,11)(195,10)(200,00)(250,01)(260,00)(285,10)(310,11)(320,10)(350,00)(360,01)(385,00)(395,10)(410,00)(415,01)(430,00)</signal>" + "\n");
		vhdlLabman.saveFile(sklopFjaTb.getId(), sklopFjaTb.getContent());

		SimulationResult simRes = vhdlLabman.runSimulation(sklopFjaTb.getId());
		System.out.println("SimRes: "+simRes);
	}
	
	private static Extractor.VHDLSourceProvider provider = null;
	
	@BeforeClass
	public static void init() {

		provider = new Extractor.VHDLSourceProvider() {
	
			public String provide(String identifier) throws Exception {
				int n = Integer.parseInt(identifier);
				
				switch(n) {
				case 0:
					return
					"library ieee;\n"+
					"use ieee.std_logic_1164.all;\n"+
					" \n"+
					"ENTITY fja_a IS PORT (\n"+
					"  a,b,c,d: IN  std_logic;\n"+
					"  f: OUT std_logic\n"+
					");\n"+
					"END fja_a;\n"+
					" \n"+
					"ARCHITECTURE strukturna OF fja_a IS\n"+
					"  signal nota, notb, notc, notd: std_logic;\n"+
					"  signal prod1, prod2, prod3, prod4, prod5: std_logic;\n"+
					"BEGIN\n"+
					"\n"+
					"  nota <= not a after 10 ns;\n"+
					"  notb <= not b after 10 ns;\n"+
					"  notc <= not c after 10 ns;\n"+
					"  notd <= not d after 10 ns;\n"+
					"  \n"+
					"  prod1 <= nota and notb and c and notd after 10 ns;\n"+
					"  prod2 <= a and b and notc and notd after 10 ns;\n"+
					"  prod3 <= notb and notc and notd after 10 ns;\n"+
					"  prod4 <= a and b and d after 10 ns;\n"+
					"  prod5 <= b and notc and d after 10 ns;\n"+
					"\n"+
					"  f <= prod1 or prod2 or prod3 or prod4 or prod5 after 10 ns;\n"+
					"    \n"+
					"END strukturna;\n"+
					"\n";
	
				case 1:
					return "library ieee;\n"+
					"use ieee.std_logic_1164.all;\n"+
					" \n"+
					"entity fja_a_tb is\n"+
					"end fja_a_tb;\n"+
					" \n"+
					"architecture strukturna of fja_a_tb is\n"+
					"  signal a,b,c,d,f: std_logic;\n"+
					"begin\n"+
					" \n"+
					"  uut: entity work.fja_a port map (a,b,c,d,f);\n"+
					" \n"+
					"  process\n"+
					"  begin\n"+
					"    a <= '0';\n"+
					"    b <= '0';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    a <= '0';\n"+
					"    b <= '0';\n"+
					"    c <= '1';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					"\n"+
					"    a <= '0';\n"+
					"    b <= '0';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    a <= '1';\n"+
					"    b <= '0';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    a <= '1';\n"+
					"    b <= '1';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    a <= '1';\n"+
					"    b <= '1';\n"+
					"    c <= '0';\n"+
					"    d <= '1';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    a <= '1';\n"+
					"    b <= '1';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					"\n"+
					"    a <= '1';\n"+
					"    b <= '0';\n"+
					"    c <= '0';\n"+
					"    d <= '0';\n"+
					"    wait for 100 ns;\n"+
					" \n"+
					"    wait;\n"+
					"  end process;\n"+
					"   \n"+
					"end strukturna;\n"+
					"\n";
	
					default:
						throw new IndexOutOfBoundsException("Illegal identifier!");
				}
			}
		};
	}

}
