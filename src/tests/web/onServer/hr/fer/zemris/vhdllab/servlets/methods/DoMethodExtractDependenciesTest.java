package hr.fer.zemris.vhdllab.servlets.methods;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoMethodExtractDependenciesTest {
	
	private static ManagerProvider mprov;
	private static RegisteredMethod regMethod;
	private static String method;
	private Properties prop;
	
	private static Project project;
	private static File file1;
	private static File file2;
	private static File file3;
	
	@BeforeClass
	public static void init() throws ServiceException {
		mprov = new SampleManagerProvider();
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		project = labman.createNewProject("TestProjectName", Long.valueOf(1000));
		file1 = labman.createNewFile(project, "TestFileName_1", File.FT_VHDLSOURCE);
		String content1 = "library IEEE;\n"+
			"use IEEE.STD_LOGIC_1164.ALL;\n\n"+
			"entity func is port (\n"+
			"\ta, b, c, d: in std_logic;\n"+
			"\tf: out std_logic\n );"+
			"end func;\n\n"+
			"architecture Behavioral of func is\n"+
			"signal	f1, f2, f3: std_logic;\n"+
			"begin\n"+
			"process(a, b, c, d)\n"+
			"begin\n"+
			"\tf1 <= ( not a and not b and not c and d) or\n"+
				"\t\t( not a and not b and c and d) or\n"+
				"\t\t( not a and b and not c and d) or\n"+
				"\t\t( a and not b and not c and d) or\n"+
				"\t\t( a and not b and c and not d) or\n"+
				"\t\t( a and b and not c and not d);\n"+
			"\tf2 <= ( a and b and c) or\n"+
			"\t\t( a and c and d) or\n"+
			"\t\t( not a and not b and c);\n"+
			"\tf3 <= ( a or b or not c or d) and\n"+
				"\t\t( a or not b or c or d) and\n"+
				"\t\t( not a or b or c or not d) and\n"+
				"\t\t( not a or b or not c or not d) and\n"+
				"\t\t( not a or not b or c or d) and\n"+
				"\t\t( not a or not b or not c or not d);\n"+
			"\tf <= f1 and (f2 or f3);\n"+
			"end process;\n\n"+
			"end Behavioral;\n";
		labman.saveFile(file1.getId(), content1);
		file2 = labman.createNewFile(project, "TestFileName_2", File.FT_VHDLSOURCE);
		String content2 = "";
		labman.saveFile(file2.getId(), content2);
		file3 = labman.createNewFile(project, "TestFileName_3", File.FT_VHDLTB);
		String content3 = "<measureUnit>ns</measureUnit>\n" +
			"<duration>1000</duration>\n" +
			"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
			"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
			"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
			"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
			"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
			"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>";
		labman.saveFile(file3.getId(), content3);
		Set<File> files = new TreeSet<File>();
		files.add(file1);
		files.add(file2);
		files.add(file3);
		project.setFiles(files);
		labman.saveProject(project);
		regMethod = new DoMethodExtractDependencies();
		method = MethodConstants.MTD_EXTRACT_DEPENDENCIES;
	}
	
	@Before
	public void initEachTest() {
		prop = new Properties();
		prop.setProperty(MethodConstants.PROP_METHOD, method);
	}
	
	/**
	 * Missing File ID argument.
	 */
	@Test
	public void run() {
		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * File ID argument is not number.
	 */
	@Test
	public void run2() {
		prop.setProperty(MethodConstants.PROP_FILE_ID, "n");
		
		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DoMethodExtractDependenciesTest.class);
	}
}