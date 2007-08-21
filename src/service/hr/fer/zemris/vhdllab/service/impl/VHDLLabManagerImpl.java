package hr.fer.zemris.vhdllab.service.impl;

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
import hr.fer.zemris.vhdllab.preferences.global.PreferencesParser;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;
import hr.fer.zemris.vhdllab.service.dependency.automat.AUTDependancy;
import hr.fer.zemris.vhdllab.service.dependency.schema.SchemaDependency;
import hr.fer.zemris.vhdllab.service.extractor.ICircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.extractor.automat.AutCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.extractor.schema.SchemaCircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.service.generator.automat.VHDLGenerator;
import hr.fer.zemris.vhdllab.service.generator.schema.SchemaVHDLGenerator;
import hr.fer.zemris.vhdllab.simulators.ISimulator;
import hr.fer.zemris.vhdllab.utilities.FileUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Extractor;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.tb.TestBenchDependencyExtractor;
import hr.fer.zemris.vhdllab.vhdl.tb.Testbench;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class VHDLLabManagerImpl implements VHDLLabManager {

	private FileDAO fileDAO;
	private GlobalFileDAO globalFileDAO;
	private ProjectDAO projectDAO;
	private UserFileDAO userFileDAO;

	public VHDLLabManagerImpl() {
	}

	public CompilationResult compile(Long fileId) throws ServiceException {
		File file = loadFile(fileId);
		List<File> deps = extractDependencies(file);
		List<File> otherFiles = new ArrayList<File>();
		for (File f : deps) {
			if (f.getFileType().equals(FileTypes.FT_PREDEFINED)) {
				otherFiles.add(getPredefinedFile(f.getFileName(), true));
			}
		}
		deps.removeAll(otherFiles);

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				"compiler.properties");
		if (is == null) {
			throw new ServiceException(
					"Internal error. Properties file not found.");
		}
		Properties allCompilerProps = new Properties();
		try {
			allCompilerProps.load(is);
		} catch (Exception ignorable) {
		}

		String sClass = allCompilerProps.getProperty("compiler.class");
		if (sClass == null) {
			throw new ServiceException(
					"File compiler.properties is incomplete.");
		}
		Properties compilerProps = new Properties();
		for (Object key : allCompilerProps.keySet()) {
			String k = (String) key;
			if (k.startsWith("compiler.params.")) {
				compilerProps.setProperty(k.substring(16), allCompilerProps
						.getProperty(k));
			}
		}

		ICompiler compiler = null;
		try {
			Constructor<?> c = Class.forName(sClass).getConstructor(
					new Class[] { Properties.class });
			compiler = (ICompiler) c
					.newInstance(new Object[] { compilerProps });
		} catch (Exception ex) {
			throw new ServiceException("Could not start compiler wrapper.");
		}

		return compiler.compile(deps, otherFiles, file, this);
	}

	public File createNewFile(Project project, String fileName, String fileType)
			throws ServiceException {
		File file = new File();
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setProject(project);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return file;
	}

	public GlobalFile createNewGlobalFile(String name, String type)
			throws ServiceException {
		GlobalFile file = new GlobalFile();
		file.setName(name);
		file.setType(type);

		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}

		return file;
	}

	public Project createNewProject(String projectName, String ownerId)
			throws ServiceException {
		Project project = new Project();
		project.setProjectName(projectName);
		project.setOwnerId(ownerId);
		project.setFiles(new HashSet<File>(0));
		try {
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return project;
	}

	public UserFile createNewUserFile(String ownerId, String name, String type)
			throws ServiceException {
		UserFile file = new UserFile();
		file.setOwnerID(ownerId);
		file.setName(name);
		file.setType(type);

		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}

		return file;
	}

	public void deleteFile(Long fileId) throws ServiceException {
		try {
			File file = fileDAO.load(fileId);
			fileDAO.delete(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void deleteGlobalFile(Long fileId) throws ServiceException {
		try {
			GlobalFile file = globalFileDAO.load(fileId);
			globalFileDAO.delete(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void deleteProject(Long projectId) throws ServiceException {
		try {
			Project project = projectDAO.load(projectId);
			projectDAO.delete(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void deleteUserFile(Long fileId) throws ServiceException {
		try {
			UserFile file = userFileDAO.load(fileId);
			userFileDAO.delete(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsFile(Long projectId, String fileName)
			throws ServiceException {
		try {
			return fileDAO.exists(projectId, fileName);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsGlobalFile(String name) throws ServiceException {
		try {
			return globalFileDAO.exists(name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.exists(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsProject(String ownerId, String projectName)
			throws ServiceException {
		try {
			return projectDAO.exists(ownerId, projectName);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.exists(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public boolean existsUserFile(String ownerId, String name)
			throws ServiceException {
		try {
			return userFileDAO.exists(ownerId, name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public CircuitInterface extractCircuitInterface(File file)
			throws ServiceException {
		if (file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)
				|| file.getFileType().equals(FileTypes.FT_PREDEFINED)) {
			return Extractor.extractCircuitInterface(file.getContent());
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_TB)) {
			List<File> vhdlSources = extractDependencies(file);
			if (vhdlSources.size() != 2) {
				throw new ServiceException(
						"Testbench depends on more then one or unknown file!");
			}
			vhdlSources.remove(file);

			File source = vhdlSources.get(0);
			return Extractor.extractCircuitInterface(source.getContent());
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			ICircuitInterfaceExtractor extractor = new AutCircuitInterfaceExtractor();
			return extractor.extractCircuitInterface(file);
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_STRUCT_SCHEMA)) {
			ICircuitInterfaceExtractor extractor = new SchemaCircuitInterfaceExtractor();
			return extractor.extractCircuitInterface(file);
		} else {
			throw new ServiceException("No extractor for file type: "
					+ file.getFileType());
		}
	}

	public List<File> extractDependencies(File file) throws ServiceException {
		Set<File> visitedFiles = new HashSet<File>();
		List<File> notYetAnalyzedFiles = new LinkedList<File>();
		notYetAnalyzedFiles.add(file);
		visitedFiles.add(file);
		while (!notYetAnalyzedFiles.isEmpty()) {
			File f = notYetAnalyzedFiles.remove(0);
			List<File> dependancies = extractDependenciesDisp(f);
			for (File dependancy : dependancies) {
				if (visitedFiles.contains(dependancy))
					continue;
				notYetAnalyzedFiles.add(dependancy);
				visitedFiles.add(dependancy);
			}
		}
		return new ArrayList<File>(visitedFiles);
	}

	public List<File> extractDependenciesDisp(File file)
			throws ServiceException {
		IDependency depExtractor = null;
		if (file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)) {
			depExtractor = new VHDLDependencyExtractor();
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_TB)) {
			depExtractor = new TestBenchDependencyExtractor();
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			depExtractor = new AUTDependancy();
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_STRUCT_SCHEMA)) {
			depExtractor = new SchemaDependency();
		} else if (file.getFileType().equals(FileTypes.FT_PREDEFINED)) {
			return Collections.emptyList();
		} else {
			throw new ServiceException("FileType " + file.getFileType()
					+ " has no registered dependency extractors!");
		}
		List<File> deps = depExtractor.extractDependencies(file, this);
		if (deps == null)
			return Collections.emptyList();
		return deps;
	}

	public Hierarchy extractHierarchy(Project project) throws ServiceException {
		Hierarchy h;
		try {
			h = Extractor.extractHierarchy(project, this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return h;
	}

	public File findFileByName(Long projectId, String name)
			throws ServiceException {
		try {
			return fileDAO.findByName(projectId, name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<GlobalFile> getAllGlobalFiles() throws ServiceException {
		List<GlobalFile> files = null;
		try {
			files = globalFileDAO.getAll();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return files;
	}

	public List<GlobalFile> findGlobalFilesByType(String type)
			throws ServiceException {
		List<GlobalFile> files = null;
		try {
			files = globalFileDAO.findByType(type);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return files;
	}

	@Override
	public GlobalFile findGlobalFileByName(String name) throws ServiceException {
		GlobalFile file = null;
		try {
			file = globalFileDAO.findByName(name);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return file;
	}

	public List<Project> findProjectsByUser(String userId)
			throws ServiceException {
		List<Project> projects = null;
		try {
			projects = projectDAO.findByUser(userId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
		return projects;
	}

	public List<UserFile> findUserFilesByUser(String userId)
			throws ServiceException {
		try {
			List<UserFile> files = userFileDAO.findByUser(userId);
			for (GlobalFile globalFile : getAllGlobalFiles()) {
				boolean found = false;
				for (UserFile userFile : files) {
					if (userFile.getName().equalsIgnoreCase(
							globalFile.getName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					UserFile uf = synchronizeUserFile(globalFile, userId);
					files.add(uf);
				}
			}
			return files;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public UserFile findUserFileByName(String userId, String name)
			throws ServiceException {
		UserFile file = null;
		if (existsUserFile(userId, name)) {
			try {
				file = userFileDAO.findByName(userId, name);
			} catch (DAOException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage(), e);
			}
		} else {
			GlobalFile globalFile = findGlobalFileByName(name);
			file = synchronizeUserFile(globalFile, userId);
		}
		return file;
	}

	private UserFile synchronizeUserFile(GlobalFile file, String userId)
			throws ServiceException {
		UserFile uf = createNewUserFile(userId, file.getName(), file.getType());
		Property property = PreferencesParser.parseProperty(file.getContent());
		saveUserFile(uf.getId(), property.getData().getDefaultValue());
		return uf;
	}

	public String generateVHDL(File file) throws ServiceException {
		if (file.getFileType().equals(FileTypes.FT_VHDL_SOURCE)
				|| file.getFileType().equals(FileTypes.FT_PREDEFINED)) {
			return file.getContent();
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_TB)) {
			IVHDLGenerator generator = new Testbench();
			return generator.generateVHDL(file, this);
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_AUTOMAT)) {
			IVHDLGenerator generator = new VHDLGenerator();
			return generator.generateVHDL(file, this);
		} else if (file.getFileType().equals(FileTypes.FT_VHDL_STRUCT_SCHEMA)) {
			IVHDLGenerator generator = new SchemaVHDLGenerator();
			return generator.generateVHDL(file, this);
		} else
			throw new ServiceException("FileType " + file.getFileType()
					+ " has no registered vhdl generators!");
	}

	public FileDAO getFileDAO() {
		return fileDAO;
	}

	public GlobalFileDAO getGlobalFileDAO() {
		return globalFileDAO;
	}

	public Project findProjectByName(String userId, String projectName)
			throws ServiceException {
		List<Project> projects = findProjectsByUser(userId);
		for (Project p : projects) {
			if (p.getProjectName().equals(projectName)) {
				return p;
			}
		}
		throw new ServiceException("No such project!");
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public UserFileDAO getUserFileDAO() {
		return userFileDAO;
	}

	public File loadFile(Long fileId) throws ServiceException {
		try {
			return fileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public GlobalFile loadGlobalFile(Long fileId) throws ServiceException {
		try {
			return globalFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public Project loadProject(Long projectId) throws ServiceException {
		try {
			return projectDAO.load(projectId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public UserFile loadUserFile(Long fileId) throws ServiceException {
		try {
			return userFileDAO.load(fileId);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void renameFile(Long fileId, String newName) throws ServiceException {
		try {
			File file = fileDAO.load(fileId);
			file.setFileName(newName);
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void renameGlobalFile(Long fileId, String newName)
			throws ServiceException {
		try {
			GlobalFile file = globalFileDAO.load(fileId);
			file.setName(newName);
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void renameProject(Long projectId, String newName)
			throws ServiceException {
		try {
			Project project = projectDAO.load(projectId);
			project.setProjectName(newName);
			projectDAO.save(project);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public SimulationResult runSimulation(Long fileId) throws ServiceException {
		File file = loadFile(fileId);
		// Pretpostavka: file je po tipu VHDL Source samog testbencha
		List<File> deps = extractDependencies(file);
		List<File> otherFiles = new ArrayList<File>();
		for (File f : deps) {
			if (f.getFileType().equals(FileTypes.FT_PREDEFINED)) {
				otherFiles.add(getPredefinedFile(f.getFileName(), true));
			}
		}
		deps.removeAll(otherFiles);

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				"simulator.properties");
		if (is == null) {
			throw new ServiceException(
					"Internal error. Properties file not found.");
		}
		Properties allSimProps = new Properties();
		try {
			allSimProps.load(is);
		} catch (Exception ignorable) {
		}

		String sClass = allSimProps.getProperty("simulator.class");
		if (sClass == null) {
			throw new ServiceException(
					"File simulator.properties is incomplete.");
		}
		Properties simProps = new Properties();
		for (Object key : allSimProps.keySet()) {
			String k = (String) key;
			if (k.startsWith("simulator.params.")) {
				simProps.setProperty(k.substring(17), allSimProps
						.getProperty(k));
			}
		}

		ISimulator simulator = null;
		try {
			Constructor<?> c = Class.forName(sClass).getConstructor(
					new Class[] { Properties.class });
			simulator = (ISimulator) c.newInstance(new Object[] { simProps });
		} catch (Exception ex) {
			throw new ServiceException("Could not start simulator wrapper.");
		}

		return simulator.simulate(deps, otherFiles, file, this);
	}

	public void saveFile(Long fileId, String content) throws ServiceException {
		File file = loadFile(fileId);
		file.setContent(content);
		try {
			fileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void saveGlobalFile(Long fileId, String content)
			throws ServiceException {
		GlobalFile file = loadGlobalFile(fileId);
		file.setContent(content);
		try {
			globalFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void saveProject(Project p) throws ServiceException {
		try {
			projectDAO.save(p);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void saveUserFile(Long fileId, String content)
			throws ServiceException {
		UserFile file = loadUserFile(fileId);
		file.setContent(content);
		try {
			userFileDAO.save(file);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public void setFileDAO(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	public void setGlobalFileDAO(GlobalFileDAO globalFileDAO) {
		this.globalFileDAO = globalFileDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setUserFileDAO(UserFileDAO userFileDAO) {
		this.userFileDAO = userFileDAO;
	}

	public File getPredefinedFile(String fileName, boolean retrieveFileBody)
			throws ServiceException {
		if (fileName == null) {
			throw new ServiceException("File name can not be null.");
		}
		File f = new File();
		f.setFileName(fileName);
		f.setFileType(FileTypes.FT_PREDEFINED);
		if (retrieveFileBody) {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("predefinedFiles.properties");
			Properties p = FileUtil.getProperties(is);
			if (p == null) {
				throw new ServiceException("Can not read properties file.");
			}
			String predefDir = p.getProperty("predefined.files.tmpDir");
			String pathToFile = FileUtil.mergePaths(predefDir, fileName);
			f.setContent(FileUtil.readFile(pathToFile));
		}
		return f;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.service.VHDLLabManager#saveErrorMessage(java.lang.String, java.lang.String)
	 */
	public void saveErrorMessage(String userId, String content)
			throws ServiceException {
		if (userId == null) {
			throw new ServiceException("User identifier cant be null.");
		}
		if (content == null) {
			throw new ServiceException("Content cant be null.");
		}

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				"applicationExceptions.properties");
		Properties p = FileUtil.getProperties(is);
		if (p == null) {
			throw new ServiceException("Can not read properties file.");
		}
		String exDir = p.getProperty("exceptions.dir");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		String name = "client_" + userId + "_" + formatter.format(date)
				+ ".log";
		String pathToFile = FileUtil.mergePaths(exDir, name);
		java.io.File file = new java.io.File(pathToFile);
		try {
			file.createNewFile();
			OutputStream os = new FileOutputStream(file);
			FileUtil.appendToFile(content, os, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}