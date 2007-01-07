package hr.fer.zemris.vhdllab.service.impl.dummy;

import hr.fer.zemris.vhdllab.compilers.ICompiler;
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
import hr.fer.zemris.vhdllab.service.dependency.automat.AUTDependancy;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.extractor.automat.AutCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.service.generator.automat.VHDLGenerator;
import hr.fer.zemris.vhdllab.simulators.ISimulator;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.tb.TestBenchDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.tb.Testbench;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class VHDLLabManagerImpl implements VHDLLabManager {
	
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	
	public VHDLLabManagerImpl() {}
	
	public CompilationResult compile(Long fileId) throws ServiceException {
		File file = loadFile(fileId);
		List<File> deps = extractDependencies(file);
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("compiler.properties");
		if(is == null) {
			throw new ServiceException("Internal error. Properties file not found.");
		}
		Properties allCompilerProps = new Properties();
		try { 
			allCompilerProps.load(is); 
		} catch(Exception ignorable) {
		}
		
		String sClass = allCompilerProps.getProperty("compiler.class");
		if(sClass==null) {
			throw new ServiceException("File compiler.properties is incomplete.");
		}
		Properties compilerProps = new Properties();
		for(Object key : allCompilerProps.keySet()) {
			String k = (String)key;
			if(k.startsWith("compiler.params.")) {
				compilerProps.setProperty(k.substring(16), allCompilerProps.getProperty(k));
			}
		}
		
		ICompiler compiler = null;
		try {
			Constructor c = Class.forName(sClass).getConstructor(new Class[] {Properties.class});
			compiler = (ICompiler)c.newInstance(new Object[] {compilerProps});
		} catch(Exception ex) {
			throw new ServiceException("Could not start compiler wrapper.");
		}
		
		return compiler.compile(deps, null, file, this);
		
		// This below was previous implementation
		/*
		String vcdResult = "src/service/hr/fer/zemris/vhdllab/vhdl/simulations/adder2.vcd";
		VcdParser vcd = new VcdParser(vcdResult);
		vcd.parse();
		String waveform = vcd.getResultInString();
		SimulationResult result = new SimulationResult(0, true, new ArrayList<SimulationMessage>(), waveform);
		return result;
		*/
		//return new CompilationResult(0, true, new ArrayList<CompilationMessage>());
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
		} else if(file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			IVHDLGenerator generator = new VHDLGenerator();
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

	public SimulationResult runSimulation(Long fileId) throws ServiceException {
		File file = loadFile(fileId);
		// Pretpostavka: file je po tipu VHDL Source samog testbencha
		List<File> deps = extractDependencies(file);
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("simulator.properties");
		if(is == null) {
			throw new ServiceException("Internal error. Properties file not found.");
		}
		Properties allSimProps = new Properties();
		try { 
			allSimProps.load(is); 
		} catch(Exception ignorable) {
		}
		
		String sClass = allSimProps.getProperty("simulator.class");
		if(sClass==null) {
			throw new ServiceException("File simulator.properties is incomplete.");
		}
		Properties simProps = new Properties();
		for(Object key : allSimProps.keySet()) {
			String k = (String)key;
			if(k.startsWith("simulator.params.")) {
				simProps.setProperty(k.substring(17), allSimProps.getProperty(k));
			}
		}
		
		ISimulator simulator = null;
		try {
			Constructor c = Class.forName(sClass).getConstructor(new Class[] {Properties.class});
			simulator = (ISimulator)c.newInstance(new Object[] {simProps});
		} catch(Exception ex) {
			throw new ServiceException("Could not start simulator wrapper.");
		}
		
		return simulator.simulate(deps, null, file, this);
		
		// This below was previous implementation
		/*
		String vcdResult = "src/service/hr/fer/zemris/vhdllab/vhdl/simulations/adder2.vcd";
		VcdParser vcd = new VcdParser(vcdResult);
		vcd.parse();
		String waveform = vcd.getResultInString();
		SimulationResult result = new SimulationResult(0, true, new ArrayList<SimulationMessage>(), waveform);
		return result;
		*/
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
		project.setOwnerId(ownerId);
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
	
	public CircuitInterface extractCircuitInterface(File file) throws ServiceException {
		if(file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)) {
			return Extractor.extractCircuitInterface(file.getContent());
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)) {
			List<File> vhdlSources = extractDependencies(file);
			if(vhdlSources.size() != 1) {
				throw new ServiceException("Testbench depends on more then one or unknown file!");
			}
			
			File source = vhdlSources.get(0);
			return Extractor.extractCircuitInterface(source.getContent());
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			ICircuitInterfaceExtractor extractor = new AutCircuitInterfaceExtractor();
			return extractor.extractCircuitInterface(file);
		} else {
			return new DefaultCircuitInterface("cicinc");
		}
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
		if(file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)) {
			depExtractor = new VHDLDependencyExtractor();
		} else if(file.getFileType().equals(FileTypes.FT_VHDL_TB)) {
			depExtractor = new TestBenchDependencyExtractor();
		} else if(file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			depExtractor = new AUTDependancy();
		} else {
			throw new ServiceException("FileType "+file.getFileType()+" has no registered dependency extractors!");
		}
		List<File> deps = depExtractor.extractDependencies(file, this);
		if(deps == null) return Collections.emptyList();
		return deps;
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.service.VHDLLabManager#extractHierarchy(hr.fer.zemris.vhdllab.model.Project)
	 */
	public Hierarchy extractHierarchy(Project project) throws ServiceException {
		Hierarchy h;
		try {
			h = Extractor.extractHierarchy(project, this);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return h;
	}
	
	/**
	 * Privider class for used whern invoking
	 * {@link Extractor#extractHierarchy(java.util.Collection,
	 * 		hr.fer.zemris.vhdllab.vhdl.model.Extractor.VHDLSourceProvider)}
	 * @author Miro Bezjak
	 */
	private class VHDLProvider implements Extractor.VHDLSourceProvider {
		
		Properties prop;
		
		/**
		 * Constructor.
		 */
		public VHDLProvider() {
			prop = new Properties();
		}
		
		/**
		 * Adds a vhdl source content so that it can be provided by
		 * {@link #provide(String)} method.
		 * @param identifier identifier which indentifies vhdl source
		 * @param content content of vhdl source
		 */
		public void addSource(String identifier, String content) {
			if(identifier == null) {
				throw new NullPointerException("Identifier can not be null.");
			}
			if(content == null) {
				throw new NullPointerException("Content can not be null.");
			}
			prop.setProperty(identifier, content);
		}
		
		/* (non-Javadoc)
		 * @see hr.fer.zemris.vhdllab.vhdl.model.Extractor.VHDLSourceProvider#provide(java.lang.String)
		 */
		public String provide(String identifier) throws Exception {
			return prop.getProperty(identifier);
		}
	}
}