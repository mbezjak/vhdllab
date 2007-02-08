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

public class GenerateVHDLTest {

	@Test
	public void generateVHDL() throws Exception {
		VHDLLabManagerImpl vhdlLabman = new VHDLLabManagerImpl();
		vhdlLabman.setFileDAO(new FileDAOMemoryImpl());
		vhdlLabman.setGlobalFileDAO(new GlobalFileDAOMemoryImpl());
		vhdlLabman.setProjectDAO(new ProjectDAOMemoryImpl());
		vhdlLabman.setUserFileDAO(new UserFileDAOMemoryImpl());
		
		Project proj = vhdlLabman.createNewProject("testProject", "user1");
		File automat = vhdlLabman.createNewFile(proj,"Automat1",FileTypes.FT_VHDL_AUTOMAT);
		automat.setContent(provider.provide("0"));
		vhdlLabman.saveFile(automat.getId(), automat.getContent());
		
		String vhdl = null;
		CompilationResult result = null;
		try {
			vhdl = vhdlLabman.generateVHDL(automat);
			result = vhdlLabman.compile(automat.getId());
		} catch (RuntimeException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(vhdl);
		System.out.println(result);
	}
	
	private static Extractor.VHDLSourceProvider provider;
	
	@BeforeClass
	public static void init() {

		provider = new Extractor.VHDLSourceProvider() {
	
			public String provide(String identifier) throws Exception {
				int n = Integer.parseInt(identifier);
				
				switch(n) {
				case 0:
					return
					"<Automat>" + "\n" +
					"<Podatci_Sklopa>" + "\n" +
						"<Ime>Automat1</Ime>" + "\n" +
						"<Tip>Moore</Tip>" + "\n" +
						"<Interfac>a in std_logic" + "\n" +
				"b out std_logic" + "\n" +
						"</Interfac>" + "\n" +
						"<Pocetno_Stanje>A</Pocetno_Stanje>" + "\n" +
						"<Reset>1</Reset>" + "\n" + 
						"<Clock>falling_edge</Clock>" + "\n" +
					"</Podatci_Sklopa>" + "\n" +

					"<Stanje>" + "\n" +
						"<Ime>A</Ime>" + "\n" +
						"<Izlaz>0</Izlaz>" + "\n" +
						"<Ox>30</Ox>" + "\n" +
						"<Oy>30</Oy>" + "\n" +
					"</Stanje>" + "\n" +

					"<Stanje>" + "\n" +
						"<Ime>B</Ime>" + "\n" +
						"<Izlaz>1</Izlaz>" + "\n" +
						"<Ox>100</Ox>" + "\n" +
						"<Oy>100</Oy>" + "\n" +
					"</Stanje>" + "\n" +

					"<Stanje>" + "\n" +
						"<Ime>C</Ime>" + "\n" +
						"<Izlaz>1</Izlaz>" + "\n" +
						"<Ox>30</Ox>" + "\n" +
						"<Oy>100</Oy>" + "\n" +
					"</Stanje>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>A</Iz>" + "\n" +
						"<U>B</U>" + "\n" +
						"<Pobuda>1</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>A</Iz>" + "\n" +
						"<U>C</U>" + "\n" +
						"<Pobuda>0</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>B</Iz>" + "\n" +
						"<U>A</U>" + "\n" +
						"<Pobuda>1</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>B</Iz>" + "\n" +
						"<U>C</U>" + "\n" +
						"<Pobuda>0</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>C</Iz>" + "\n" +
						"<U>C</U>" + "\n" +
						"<Pobuda>0</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +

					"<Prijelaz>" + "\n" +
						"<Iz>C</Iz>" + "\n" +
						"<U>B</U>" + "\n" +
						"<Pobuda>1</Pobuda>" + "\n" +
					"</Prijelaz>" + "\n" +
				"</Automat>";
	
					default:
						throw new IndexOutOfBoundsException("Illegal identifier!");
				}
			}
		};
	}
}
