package hr.fer.zemris.vhdllab.service.impl.dummy;

import hr.fer.zemris.vhdllab.dao.DAOException;
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
import java.util.List;
import java.util.Set;

public class VHDLLabManagerImpl implements VHDLLabManager {
	
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	
	public VHDLLabManagerImpl() {}
	
	public CompilationResult compile(Long fileId) {
		return new CompilationResult(0, true, new ArrayList<CompilationMessage>());
	}

	public File createNewFile(Project project, String fileName, String fileType) throws ServiceException {
		File file = new File();
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setProject(project);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return file;
	}

	public List<Project> findProjectsByUser(Long userId) throws ServiceException {
		List<Project> projects = null;
		try {
			projects = projectDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return projects;
	}

	public String generateVHDL(File file) throws ServiceException {
		if(file.getFileType().equals(File.FT_VHDLTB)) {
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
				throw new ServiceException();
			}
			
			return writted;
		}
		else return "";
	}

	public File loadFile(Long fileId) throws ServiceException {
		try {
			return fileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Project loadProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.load(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameFile(Long fileId, String newName) throws ServiceException {
		try {
			File file = fileDAO.load(fileId);
			file.setFileName(newName);
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameProject(Long projectId, String newName) throws ServiceException {
		try {
			Project project = projectDAO.load(projectId);
			project.setProjectName(newName);
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public SimulationResult runSimulation(Long fileId) {
		return new SimulationResult(0, true, new ArrayList<SimulationMessage>(), "");
	}

	public void saveFile(Long fileId, String content) throws ServiceException {
		File file = loadFile(fileId);
		file.setContent(content);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveProject(Project p) throws ServiceException {
		try {
			projectDAO.save(p);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
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
		Set<File> files = null;
		try {
			files = projectDAO.load(projectId).getFiles();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		if(files==null) return false;
		for(File f : files) {
			if(f.getFileName().equals(fileName)) return true;
		}
		return false;
	}

	public boolean existsProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.load(projectId) != null;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Project createNewProject(String projectName, Long ownerId) throws ServiceException {
		Project project = new Project();
		project.setProjectName(projectName);
		project.setOwnerID(ownerId);
		try {
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return project;
	}
}