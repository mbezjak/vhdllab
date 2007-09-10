package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.communicaton.methods.CompileFileMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.CreateFileMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.CreateProjectMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.DeleteFileMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.DeleteProjectMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.ExistsFile2Method;
import hr.fer.zemris.vhdllab.communicaton.methods.ExtractCircuitInterfaceMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.ExtractHierarchyMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.FindFileByNameMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.FindFilesByProjectMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.FindProjectsByUserMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.FindUserFilesByUserMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.GenerateVHDLMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadFileContentMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadFileNameMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadFileTypeMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadPredefinedFileContentMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadProjectNameMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadUserFileContentMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.LoadUserFileNameMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.ReportApplicationErrorMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.SaveFileMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.SaveUserFileMethod;
import hr.fer.zemris.vhdllab.communicaton.methods.SimulateFileMethod;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.model.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Communicator {

	private Initiator initiator;
	private Cache cache;

	public Communicator(Initiator initiator) {
		if (initiator == null)
			throw new NullPointerException("Initiator can not be null.");
		cache = new Cache();
		this.initiator = initiator;
	}

	public void init() throws UniformAppletException {
		loadUserPreferences();
		// FIXME TMP SOLUTION
		loadFileIds();
	}

	private void loadFileIds() throws UniformAppletException {
		/*
		 * this is a fix for loadFileContent method. it breaks when system
		 * container tries to open editors (stored in a session). because
		 * critical methods: getAllProjects and findFilesByProject was still not
		 * called nothing is cached so loadFileContent method assumes that such
		 * file does not exist.
		 */
		for (String projectName : getAllProjects()) {
			for (String fileName : findFilesByProject(projectName)) {
				// this is just extra it has nothing to do with the actual bug
				loadFileType(projectName, fileName);
			}
		}
	}

	public void dispose() throws UniformAppletException {
		UserPreferences preferences = UserPreferences.instance();
		for (String key : preferences.keys()) {
			String property = preferences.get(key, null);
			if (property == null) {
				continue;
			}
			Long id = cache.getIdentifierForProperty(key);
			SaveUserFileMethod method = new SaveUserFileMethod(id, property);
			initiate(method);
		}
	}

	public List<String> getAllProjects() throws UniformAppletException {
		FindProjectsByUserMethod method = new FindProjectsByUserMethod();
		initiate(method);
		List<Long> projectIdentifiers = method.getResult();

		List<String> projectNames = new ArrayList<String>();
		for (Long id : projectIdentifiers) {
			String projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				LoadProjectNameMethod loadProjectNameMethod = new LoadProjectNameMethod(
						id);
				initiate(loadProjectNameMethod);
				String name = loadProjectNameMethod.getResult();
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}

	public List<String> findFilesByProject(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null) {
			throw new UniformAppletException("Project does not exists!");
		}
		FindFilesByProjectMethod method = new FindFilesByProjectMethod(
				projectIdentifier);
		initiate(method);
		List<Long> fileIdentifiers = method.getResult();

		List<String> fileNames = new ArrayList<String>();
		for (Long id : fileIdentifiers) {
			FileIdentifier identifier = cache.getFileForIdentifier(id);
			if (identifier == null) {
				LoadFileNameMethod loadFileNameMethod = new LoadFileNameMethod(
						id);
				initiate(loadFileNameMethod);
				String name = loadFileNameMethod.getResult();
				cache.cacheItem(projectName, name, id);
				identifier = cache.getFileForIdentifier(id);
			}
			fileNames.add(identifier.getFileName());
		}
		return fileNames;
	}

	public boolean existsFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		/* return invoker.existsFile(projectIdentifier, fileName); */
		return cache.containsIdentifierFor(projectName, fileName);
	}

	public boolean existsProject(String projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		/*
		 * Long projectIdentifier = cache.getIdentifierFor(projectName);
		 * if(projectIdentifier == null) return false; return
		 * invoker.existsProject(projectIdentifier);
		 */
		return cache.containsIdentifierFor(projectName);
	}

	public void deleteFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName, fileName);
		DeleteFileMethod method = new DeleteFileMethod(fileIdentifier);
		initiate(method);
	}

	public void deleteProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName);
		DeleteProjectMethod method = new DeleteProjectMethod(fileIdentifier);
		initiate(method);
	}

	public void createProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		CreateProjectMethod method = new CreateProjectMethod(projectName);
		initiate(method);
		Long projectIdentifier = method.getResult();
		cache.cacheItem(projectName, projectIdentifier);
	}

	public void createFile(String projectName, String fileName, String type)
			throws UniformAppletException {
		createFile(projectName, fileName, type, null);
	}

	public void createFile(String projectName, String fileName, String type,
			String data) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (type == null)
			throw new NullPointerException("File type can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		CreateFileMethod method = new CreateFileMethod(projectIdentifier,
				fileName, type);
		initiate(method);

		Long fileIdentifier = method.getResult();
		cache.cacheItem(projectName, fileName, fileIdentifier);
		if (data != null) {
			saveFile(projectName, fileName, data);
		}
	}

	public void saveFile(String projectName, String fileName, String content)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (content == null)
			throw new NullPointerException("File content can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		SaveFileMethod method = new SaveFileMethod(fileIdentifier, content);
		initiate(method);
	}

	public String loadFileContent(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null) {
			// throw new UniformAppletException("File (" + fileName + "/"
			// + projectName + ") does not exists!");
			LoadPredefinedFileContentMethod method = new LoadPredefinedFileContentMethod(fileName);
			initiate(method);
			return method.getResult();
		} else {
			LoadFileContentMethod method = new LoadFileContentMethod(
					fileIdentifier);
			initiate(method);
			return method.getResult();
		}
	}

	public String loadPredefinedFileContent(String fileName)
			throws UniformAppletException {
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		LoadPredefinedFileContentMethod method = new LoadPredefinedFileContentMethod(
				fileName);
		initiate(method);
		return method.getResult();
	}

	public String loadFileType(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null) {
			return FileTypes.FT_PREDEFINED;
			// throw new UniformAppletException("File does not exists!");
		}
		String fileType = cache.getFileType(fileIdentifier);
		if (fileType == null) {
			LoadFileTypeMethod method = new LoadFileTypeMethod(fileIdentifier);
			initiate(method);
			fileType = method.getResult();
			cache.cacheFileType(fileIdentifier, fileType);
		}
		return fileType;
	}

	public Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");

		ExtractHierarchyMethod hierarhyMethod = new ExtractHierarchyMethod(
				projectIdentifier);
		initiate(hierarhyMethod);

		Hierarchy h = hierarhyMethod.getResult();
		for (Pair p : h) {
			String fileName = p.getFileName();
			Long id = cache.getIdentifierFor(projectName, fileName);
			if (id == null) {
				ExistsFile2Method existsMethod = new ExistsFile2Method(
						projectIdentifier, fileName);
				initiate(existsMethod);
				boolean exists = existsMethod.getResult().booleanValue();
				if (exists) {
					FindFileByNameMethod findMethod = new FindFileByNameMethod(
							projectIdentifier, fileName);
					initiate(findMethod);
					Long fileIdentifier = findMethod.getResult();
					cache.cacheItem(projectName, fileName, fileIdentifier);
				} else {
					// invoker.
				}
			}
		}
		return h;
	}

	public String generateVHDL(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		GenerateVHDLMethod method = new GenerateVHDLMethod(fileIdentifier);
		initiate(method);
		return method.getResult();
	}

	public CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		CompileFileMethod method = new CompileFileMethod(fileIdentifier);
		initiate(method);
		return method.getResult();
	}

	public SimulationResult runSimulation(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		SimulateFileMethod method = new SimulateFileMethod(fileIdentifier);
		initiate(method);
		return method.getResult();
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName,
			String fileName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		ExtractCircuitInterfaceMethod method = new ExtractCircuitInterfaceMethod(
				fileIdentifier);
		initiate(method);
		return method.getResult();
	}

	public void saveErrorMessage(String content) throws UniformAppletException {
		if (content == null)
			throw new NullPointerException("Content can not be null.");
		ReportApplicationErrorMethod method = new ReportApplicationErrorMethod(
				content);
		initiate(method);
	}

	private void loadUserPreferences() throws UniformAppletException {
		Properties properties = new Properties();
		FindUserFilesByUserMethod findMethod = new FindUserFilesByUserMethod();
		initiate(findMethod);
		List<Long> userFileIds = findMethod.getResult();
		for (Long id : userFileIds) {
			LoadUserFileNameMethod nameMethod = new LoadUserFileNameMethod(id);
			initiate(nameMethod);
			String name = nameMethod.getResult();
			cache.cacheUserFileItem(name, id);
			LoadUserFileContentMethod contentMethod = new LoadUserFileContentMethod(
					id);
			initiate(contentMethod);
			String data = contentMethod.getResult();
			properties.setProperty(name, data);
		}
		UserPreferences.instance().init(properties);
	}

	private void initiate(Method<? extends Serializable> method)
			throws UniformAppletException {
		initiator.initiateCall(method);
	}

}