package hr.fer.zemris.vhdllab.service.impl.dummy;

import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.tb.Testbench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class VHDLLabManagerImpl implements VHDLLabManager {
	
	private int createFile = 1001;
	private int createProject = 1001;
	
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	
	private Map<Long, File> files = new HashMap<Long, File>();
	private Map<Long, Project> projects = new HashMap<Long, Project>();
	
	public VHDLLabManagerImpl() {
		File file = new File();
		file.setId(Long.valueOf(0));
		file.setFileName("sklop");
		file.setFileType(File.FT_VHDLSOURCE);
		file.setContent("library IEEE;\n"+
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
				"end Behavioral;\n");
		
		Project project = new Project();
		project.setId(Long.valueOf(0));
		project.setOwnerID(Long.valueOf(0));
		project.setProjectName("Simple");
		project.setFiles(new TreeSet<File>());
		project.getFiles().add(file);
		
		file.setProject(project);
		files.put(file.getId(), file);
		projects.put(project.getId(), project);
		
		
		//**********************************
		File file2 = new File();
		file2.setId(Long.valueOf(1));
		file2.setFileName("sklop_tb");
		file2.setFileType(File.FT_VHDLTB);
		file2.setContent("<measureUnit>ns</measureUnit>\n" +
				"<duration>1000</duration>\n" +
				"<signal name = \"A\" type=\"scalar\">(0,0)(100, 1)(150, 0)(300,1)</signal>\n" + 
				"<signal name = \"b\" type=\"scalar\">(0,0)(200, 1)(300, z)(440, U)</signal>\n" +
				"<signal name = \"c\" type=\"scalar\" rangeFrom=\"0\" rangeTo=\"0\">(0,0)(50,1)(300,0)(400,1)</signal>\n" +
				"<signal name = \"d\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"0\">(100,1)(200,0)(300,1)(400,z)</signal>\n" +
				"<signal name = \"e\" type=\"vector\" rangeFrom=\"2\" rangeTo=\"0\">(0,000)(100, 100)(400, 101)(500,111)(600, 010)</signal>\n" + 
				"<signal name = \"f\" type=\"vector\" rangeFrom=\"1\" rangeTo=\"4\">(0,0001)(100, 1000)(200, 0110)(300, U101)(400, 1001)(500,110Z)(600, 0110)</signal>");
		
		project.getFiles().add(file2);
		
		file2.setProject(project);
		files.put(file2.getId(), file2);
		projects.put(project.getId(), project);
	}

	public CompilationResult compile(Long fileId) {
		return new CompilationResult(0, true, new ArrayList<CompilationMessage>());
	}

	public File createNewFile(Project project, String fileName, String fileType) {
		File file = new File();
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setContent("");
		file.setId(Long.valueOf(createFile++));
		file.setProject(project);
		files.put(file.getId(), file);
		return file;
	}

	public List<Project> findProjectsByUser(Long userId) {
		List<Project> projects = new ArrayList<Project>();
		projects.add(projects.get(0));
		return projects;
	}

	public String generateVHDL(File file) {
		if(file.getFileType().equals(File.FT_VHDLTB)) 
			return generateTestbenchVHDL(file);
		else return "";
	}

	public String generateShemaVHDL(File file) {
		return "";
	}

	public String generateTestbenchVHDL(File file) {
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
			return "";
		}
		
		return writted;
	}

	public String getFileName(Long fileId) {
		return files.get(fileId).getFileName();
	}

	public String getFileType(Long fileId) {
		return files.get(fileId).getFileType();
	}

	public File loadFile(Long fileId) {
		return files.get(fileId);
	}

	public Project loadProject(Long projectId) {
		return projects.get(projectId);
	}

	public void renameFile(Long fileId, String newName) {
		files.get(fileId).setFileName(newName);
	}

	public void renameProject(Long projectId, String newName) {
		projects.get(projectId).setProjectName(newName);
	}

	public SimulationResult runSimulation(Long fileId) {
		return new SimulationResult(0, true, new ArrayList<SimulationMessage>(), "");
	}

	public void saveFile(Long fileId, String content) {
		File file = new File();
		file.setId(fileId);
		file.setContent(content);
		files.put(file.getId(), file);
	}

	public void saveProject(Project p) {
		projects.put(p.getId(), p);
	}

	public FileDAO getFileDAO() {
		return fileDAO;
	}

	public void setFileDAO(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public boolean existsFile(Long projectId, String fileName) throws ServiceException {
		Set<File> files = projects.get(projectId).getFiles();
		for(File f : files) {
			if(f.getFileName().equals(fileName)) return true;
		}
		return false;
	}

	public boolean existsProject(Long projectId) throws ServiceException {
		return projects.get(projectId) != null;
	}

	public Project createNewProject(String projectName, Long ownerId) throws ServiceException {
		Project project = new Project();
		project.setId(Long.valueOf(createProject++));
		project.setProjectName(projectName);
		project.setOwnerID(ownerId);
		project.setFiles(null);
		projects.put(project.getId(), project);
		return project;
	}
}