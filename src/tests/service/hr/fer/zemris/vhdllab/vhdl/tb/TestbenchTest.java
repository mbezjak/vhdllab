package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.service.impl.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestbenchTest {
	
	private static VHDLLabManager vhdlLabman;
	private static Project project;
	private static File vhdlSource;
	private static File testbench;
	
	@BeforeClass
	public static void init() throws ServiceException {
		ManagerProvider provider = new SampleManagerProvider();
		vhdlLabman = (VHDLLabManagerImpl) provider.get(ManagerProvider.VHDL_LAB_MANAGER);
		
		project = vhdlLabman.createNewProject("testProject", "user1");
		vhdlSource = vhdlLabman.createNewFile(project,"mux41",FileTypes.FT_VHDL_SOURCE);
		testbench = vhdlLabman.createNewFile(project,"mux41_tb",FileTypes.FT_VHDL_TB);
		vhdlSource.setContent("library IEEE;" + "\n" +
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
		vhdlLabman.saveFile(vhdlSource.getId(), vhdlSource.getContent());
		testbench.setContent("<file>mux41</file>" + "\n" + 
				"<measureUnit>ns</measureUnit>" + "\n" +
				"<duration>700</duration>" + "\n" +
				"<signal name=\"E\" type=\"scalar\">(0,1)</signal>" + "\n" + 
				"<signal name=\"D\" type=\"vector\" rangeFrom=\"3\" rangeTo=\"0\">(0,0000)(20,1000)(30,1100)(40,1110)(50,1111)(60,0111)(65,0101)(70,0001)(90,0000)(95,0010)(120,1110)(135,1100)(150,0100)(155,0001)(180,0000)(190,1010)(230,0010)(235,0110)(245,0100)(255,0101)(265,1101)(295,1001)(315,1000)(325,1010)(330,0010)(340,0110)(360,1111)(375,1101)(380,1001)(385,1000)</signal>" + "\n" + 
				"<signal name=\"SEL\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"0\">(0,00)(25,10)(35,11)(70,10)(85,00)(130,01)(165,11)(195,10)(200,00)(250,01)(260,00)(285,10)(310,11)(320,10)(350,00)(360,01)(385,00)(395,10)(410,00)(415,01)(430,00)</signal>" + "\n");
		vhdlLabman.saveFile(testbench.getId(), testbench.getContent());
	}
	
	@Test
	public void writeVHDL() throws ServiceException {
		IVHDLGenerator generator = new Testbench();
		generator.generateVHDL(testbench, vhdlLabman);
	}
	
	/**
	 * File is <code>null</code>.
	 * @throws ServiceException
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDL2() throws ServiceException {
		IVHDLGenerator generator = new Testbench();
		generator.generateVHDL(null, vhdlLabman);
	}
	
	/**
	 * Vhdl Lab Manager is <code>null</code>.
	 * @throws ServiceException
	 */
	@Test(expected=NullPointerException.class)
	public void writeVHDL3() throws ServiceException {
		IVHDLGenerator generator = new Testbench();
		generator.generateVHDL(testbench, null);
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestbenchTest.class);
	}
	
}