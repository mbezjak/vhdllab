package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.dummy.FileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.GlobalFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.ProjectDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.UserFileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.impl.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

import org.junit.BeforeClass;
import org.junit.Test;

public class CompileTest {
	
	@Test
	public void compileTest() throws Exception {
		VHDLLabManagerImpl vhdlLabman = new VHDLLabManagerImpl();
		vhdlLabman.setFileDAO(new FileDAOMemoryImpl());
		vhdlLabman.setGlobalFileDAO(new GlobalFileDAOMemoryImpl());
		vhdlLabman.setProjectDAO(new ProjectDAOMemoryImpl());
		vhdlLabman.setUserFileDAO(new UserFileDAOMemoryImpl());
		
		Project proj = vhdlLabman.createNewProject("testProject", "user1");
		File sklopFja = vhdlLabman.createNewFile(proj,"fja_a",FileTypes.FT_VHDL_SOURCE);
		File sklopFjaTb = vhdlLabman.createNewFile(proj,"fja_a_tb",FileTypes.FT_VHDL_SOURCE);
		sklopFja.setContent(provider.provide("0"));
		vhdlLabman.saveFile(sklopFja.getId(), sklopFja.getContent());
		sklopFjaTb.setContent(provider.provide("1"));
		vhdlLabman.saveFile(sklopFjaTb.getId(), sklopFjaTb.getContent());

		// This should be clean:
		CompilationResult compRes = vhdlLabman.compile(sklopFjaTb.getId());
		System.out.println("CompRes: "+compRes);
		
		// This should have errors:
		sklopFja.setContent(provider.provide("2"));
		vhdlLabman.saveFile(sklopFja.getId(), sklopFja.getContent());
		compRes = vhdlLabman.compile(sklopFjaTb.getId());
		System.out.println("CompRes: "+compRes);
		
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
					
				case 2:
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
					"  nota : <= not a after 10 ns;\n"+
					"  notb <= not b after 10 ns;\n"+
					"  notc => not c after 10 ns;\n"+
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
	
	
					default:
						throw new IndexOutOfBoundsException("Illegal identifier!");
				}
			}
		};
	}

}
