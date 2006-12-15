package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;
import hr.fer.zemris.vhdllab.vhdl.tb.Testbench;
import hr.fer.zemris.vhdllab.simulators.ghdl.GHDLSimulator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class VHDLLabManagerImpl0 implements VHDLLabManager {
	
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	
	public VHDLLabManagerImpl0() {}
	
	
	
	public CompilationResult compile(Long fileId) throws ServiceException {
		
			return null; //TODO
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

	public List<Project> findProjectsByUser(String userId) throws ServiceException {
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
		if(file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)) {
			return file.getContent();
		} else if(file.getFileType().equals(FileTypes.FT_VHDL_TB)) {
			IVHDLGenerator generator = new Testbench();
			return generator.generateVHDL(file, this);
		}
		else throw new ServiceException("FileType "+file.getFileType()+" has no registered vhdl generators!");
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

	public SimulationResult runSimulation(Long fileId) throws ServiceException  {
		InputStream is = null;
		Properties prop = new Properties();
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("appProperties");
		
			
			prop.load(is);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		} finally {
			if(is!=null) {
				try { is.close(); } catch(Exception ex) {}
			}
		}
		
		File f= this.loadFile(fileId);
		
		
		List<File> dbFiles = extractDependencies(f);
		
		
		
		
		GHDLSimulator sim =new GHDLSimulator(prop);
		SimulationResult s= sim.simulate(dbFiles, null, f, this);
		return s;
		
		
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
	
	public GlobalFileDAO getGlobalFileDAO() {
		return globalFileDAO;
	}
	public void setGlobalFileDAO(GlobalFileDAO globalFileDAO) {
		this.globalFileDAO = globalFileDAO;
	}

	public UserFileDAO getUserFileDAO() {
		return userFileDAO;
	}
	public void setUserFileDAO(UserFileDAO userFileDAO) {
		this.userFileDAO = userFileDAO;
	}

	public boolean existsFile(Long projectId, String fileName) throws ServiceException {
		try {
			return fileDAO.exists(projectId,fileName);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public boolean existsProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.exists(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Project createNewProject(String projectName, String ownerId) throws ServiceException {
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

	public GlobalFile createNewGlobalFile(String name, String type) throws ServiceException {
		GlobalFile file = new GlobalFile();
		file.setName(name);
		file.setType(type);
		
		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not save global file.");
		}
		
		return file;
	}

	public UserFile createNewUserFile(String ownerId, String type) throws ServiceException {
		UserFile file = new UserFile();
		file.setOwnerID(ownerId);
		file.setType(type);
		
		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not save user file.");
		}
		
		return file;
	}

	public void deleteFile(Long fileId) throws ServiceException {
		try {
			fileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete file.");
		}
	}

	public void deleteGlobalFile(Long fileId) throws ServiceException {
		try {
			globalFileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete global file.");
		}
	}

	public void deleteProject(Long projectId) throws ServiceException {
		try {
			projectDAO.delete(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete project.");
		}
	}

	public void deleteUserFile(Long fileId) throws ServiceException {
		try {
			userFileDAO.delete(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("Can not delete user file.");
		}
	}

	public boolean existsGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public boolean existsUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<GlobalFile> findGlobalFilesByType(String type) throws ServiceException {
		List<GlobalFile> files = null;
		try {
			files = globalFileDAO.findByType(type);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return files;
	}

	public List<UserFile> findUserFilesByUser(String userId) throws ServiceException {
		List<UserFile> files = null;
		try {
			files = userFileDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return files;
	}

	public GlobalFile loadGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public UserFile loadUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void renameGlobalFile(Long fileId, String newName) throws ServiceException {
		try {
			GlobalFile file = globalFileDAO.load(fileId);
			file.setName(newName);
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveGlobalFile(Long fileId, String content) throws ServiceException {
		GlobalFile file = loadGlobalFile(fileId);
		file.setContent(content);
		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveUserFile(Long fileId, String content) throws ServiceException {
		UserFile file = loadUserFile(fileId);
		file.setContent(content);
		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}

	}

	public File findByName(Long projectId, String name) throws ServiceException {
		try {
			return fileDAO.findByName(projectId,name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
	
	public CircuitInterface extractCircuitInterface(File file){
		return new DefaultCircuitInterface("cicinc");
	}

	public List<File> extractDependencies(File file) throws ServiceException {
		Set<File> visitedFiles = new HashSet<File>();
		List<File> notYetAnalyzedFiles = new LinkedList<File>();
		notYetAnalyzedFiles.add(file);
		visitedFiles.add(file);
		while(!notYetAnalyzedFiles.isEmpty()) {
			File f = notYetAnalyzedFiles.remove(0);
			List<File> dependancies = extractDependenciesDisp(f);
			for(File dependancy : dependancies) {
				if(visitedFiles.contains(dependancy)) continue;
				notYetAnalyzedFiles.add(dependancy);
				visitedFiles.add(dependancy);
			}
		}
		return new ArrayList<File>(visitedFiles);
	}

	private List<File> extractDependenciesDisp(File file) throws ServiceException {
		IDependency depExtractor = null;
		if(file.getFileType().equals("FT_VHDL_SOURCE")) {
			depExtractor = new VHDLDependencyExtractor();
		} else {
			throw new ServiceException("FileType "+file.getFileType()+" has no registered dependency extractors!");
		}
		return depExtractor.extractDependencies(file, this);
	}






}