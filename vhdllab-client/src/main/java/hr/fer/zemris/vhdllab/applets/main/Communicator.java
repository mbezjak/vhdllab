package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.comm.methods.CompileFileMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.CreateFileMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.CreateProjectMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.DeleteFileMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.DeleteProjectMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.ExistsFile2Method;
import hr.fer.zemris.vhdllab.api.comm.methods.ExtractCircuitInterfaceMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.ExtractHierarchyMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.FindFileByNameMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.FindFilesByProjectMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.FindProjectsByUserMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.FindUserFilesByUserMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.GenerateVHDLMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.IsSuperuserMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadFileContentMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadFileNameMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadFileTypeMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadPredefinedFileContentMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadProjectNameMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadUserFileContentMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.LoadUserFileNameMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.ReportApplicationErrorMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.SaveFileMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.SaveUserFileMethod;
import hr.fer.zemris.vhdllab.api.comm.methods.SimulateFileMethod;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.main.interfaces.Initiator;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.SystemContext;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Communicator {

	private Initiator initiator;
	private Cache cache;
	private String userId;

	public Communicator(Initiator initiator) {
		if (initiator == null)
			throw new NullPointerException("Initiator can not be null.");
		cache = new Cache();
		this.initiator = initiator;
	}

	public void init() throws UniformAppletException {
	    userId = SystemContext.instance().getUserId();
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
			SaveUserFileMethod method = new SaveUserFileMethod(id, property, userId);
			initiate(method);
		}
	}

	public List<String> getAllProjects() throws UniformAppletException {
		FindProjectsByUserMethod method = new FindProjectsByUserMethod(userId);
		initiate(method);
		List<Long> projectIdentifiers = method.getResult();

		List<String> projectNames = new ArrayList<String>();
		for (Long id : projectIdentifiers) {
			String projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				LoadProjectNameMethod loadProjectNameMethod = new LoadProjectNameMethod(
						id, userId);
				initiate(loadProjectNameMethod);
				String name = loadProjectNameMethod.getResult();
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}

	public List<String> getAllProjects(String userId) throws UniformAppletException {
		FindProjectsByUserMethod method = new FindProjectsByUserMethod(userId);
		initiate(method);
		List<Long> projectIdentifiers = method.getResult();
		
		List<String> projectNames = new ArrayList<String>();
		for (Long id : projectIdentifiers) {
			String projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				LoadProjectNameMethod loadProjectNameMethod = new LoadProjectNameMethod(
						id, userId);
				initiate(loadProjectNameMethod);
				String name = loadProjectNameMethod.getResult();
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}
	
	public boolean isSuperuser() throws UniformAppletException {
		IsSuperuserMethod method = new IsSuperuserMethod(SystemContext.instance().getUserId());
		initiate(method);
		return method.getResult().booleanValue();
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
				projectIdentifier, userId);
		initiate(method);
		List<Long> fileIdentifiers = method.getResult();

		List<String> fileNames = new ArrayList<String>();
		for (Long id : fileIdentifiers) {
			FileIdentifier identifier = cache.getFileForIdentifier(id);
			if (identifier == null) {
				LoadFileNameMethod loadFileNameMethod = new LoadFileNameMethod(
						id, userId);
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
		DeleteFileMethod method = new DeleteFileMethod(fileIdentifier, userId);
		initiate(method);
	}

	public void deleteProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName);
		DeleteProjectMethod method = new DeleteProjectMethod(fileIdentifier, userId);
		initiate(method);
	}

	public void createProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		CreateProjectMethod method = new CreateProjectMethod(projectName, userId);
		initiate(method);
		Long projectIdentifier = method.getResult();
		cache.cacheItem(projectName, projectIdentifier);
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
				fileName, type, data, userId);
		initiate(method);

		Long fileIdentifier = method.getResult();
		cache.cacheItem(projectName, fileName, fileIdentifier);
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
		SaveFileMethod method = new SaveFileMethod(fileIdentifier, content, userId);
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
			LoadPredefinedFileContentMethod method = new LoadPredefinedFileContentMethod(fileName, userId);
			initiate(method);
			return method.getResult();
		} else {
			LoadFileContentMethod method = new LoadFileContentMethod(
					fileIdentifier, userId);
			initiate(method);
			return method.getResult();
		}
	}

	public String loadPredefinedFileContent(String fileName)
			throws UniformAppletException {
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		LoadPredefinedFileContentMethod method = new LoadPredefinedFileContentMethod(
				fileName, userId);
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
			return FileTypes.VHDL_PREDEFINED;
			// throw new UniformAppletException("File does not exists!");
		}
		String fileType = cache.getFileType(fileIdentifier);
		if (fileType == null) {
			LoadFileTypeMethod method = new LoadFileTypeMethod(fileIdentifier, userId);
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
				projectIdentifier, userId);
		initiate(hierarhyMethod);

		Hierarchy h = hierarhyMethod.getResult();
		for (String fileName : getAllForHierarchy(h)) {
			Long id = cache.getIdentifierFor(projectName, fileName);
			if (id == null) {
				ExistsFile2Method existsMethod = new ExistsFile2Method(
						projectIdentifier, fileName, userId);
				initiate(existsMethod);
				boolean exists = existsMethod.getResult().booleanValue();
				if (exists) {
					FindFileByNameMethod findMethod = new FindFileByNameMethod(
							projectIdentifier, fileName, userId);
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
	
	private Set<String> getAllForHierarchy(Hierarchy h) {
	    // TODO pogledat dal ovo radi. nisam siguran sto se tice case insensitivity
	    Set<String> names = new HashSet<String>(h.fileCount());
	    Set<String> namesWithoutCasing = new HashSet<String>(h.fileCount());
	    for (String f : h.getTopLevelFiles()) {
	        names.add(f);
	        namesWithoutCasing.add(f.toLowerCase());
	        Set<String> alldeps = h.getAllDependenciesForFile(f);
	        for (String dep : alldeps) {
	            if(!namesWithoutCasing.contains(dep.toLowerCase())) {
	                namesWithoutCasing.add(dep.toLowerCase());
	                names.add(dep);
	            }
                
            }
        }
	    return names;
	}

	public VHDLGenerationResult generateVHDL(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		GenerateVHDLMethod method = new GenerateVHDLMethod(fileIdentifier, userId);
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
		CompileFileMethod method = new CompileFileMethod(fileIdentifier, userId);
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
		SimulateFileMethod method = new SimulateFileMethod(fileIdentifier, userId);
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
				fileIdentifier, userId);
		initiate(method);
		return method.getResult();
	}

	public void saveErrorMessage(String content) throws UniformAppletException {
		if (content == null)
			throw new NullPointerException("Content can not be null.");
		ReportApplicationErrorMethod method = new ReportApplicationErrorMethod(
				content, userId);
		initiate(method);
	}

	private void loadUserPreferences() throws UniformAppletException {
		Properties properties = new Properties();
		FindUserFilesByUserMethod findMethod = new FindUserFilesByUserMethod(userId);
		initiate(findMethod);
		List<Long> userFileIds = findMethod.getResult();
		for (Long id : userFileIds) {
			LoadUserFileNameMethod nameMethod = new LoadUserFileNameMethod(id, userId);
			initiate(nameMethod);
			String name = nameMethod.getResult();
			cache.cacheUserFileItem(name, id);
			LoadUserFileContentMethod contentMethod = new LoadUserFileContentMethod(
					id, userId);
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