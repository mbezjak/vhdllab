package hr.fer.zemris.vhdllab.applets.main;

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
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Communicator {

	private Initiator initiator;
	private Cache cache;
	private Caseless userId;

	public Communicator(Initiator initiator) {
		if (initiator == null)
			throw new NullPointerException("Initiator can not be null.");
		cache = new Cache();
		this.initiator = initiator;
	}

	public void init() throws UniformAppletException {
	    userId = new Caseless(SystemContext.instance().getUserId());
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
		for (Caseless projectName : getAllProjects()) {
			for (Caseless fileName : findFilesByProject(projectName)) {
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
			Integer id = cache.getIdentifierForProperty(new Caseless(key));
			SaveUserFileMethod method = new SaveUserFileMethod(id, property, userId);
			initiate(method);
		}
	}

	public List<Caseless> getAllProjects() throws UniformAppletException {
		FindProjectsByUserMethod method = new FindProjectsByUserMethod(userId);
		initiate(method);
		List<Integer> projectIdentifiers = method.getResult();

		List<Caseless> projectNames = new ArrayList<Caseless>();
		for (Integer id : projectIdentifiers) {
		    Caseless projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				LoadProjectNameMethod loadProjectNameMethod = new LoadProjectNameMethod(
						id, userId);
				initiate(loadProjectNameMethod);
				Caseless name = loadProjectNameMethod.getResult();
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}

	public List<Caseless> getAllProjects(Caseless userId) throws UniformAppletException {
		FindProjectsByUserMethod method = new FindProjectsByUserMethod(userId);
		initiate(method);
		List<Integer> projectIdentifiers = method.getResult();
		
		List<Caseless> projectNames = new ArrayList<Caseless>();
		for (Integer id : projectIdentifiers) {
		    Caseless projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				LoadProjectNameMethod loadProjectNameMethod = new LoadProjectNameMethod(
						id, userId);
				initiate(loadProjectNameMethod);
				Caseless name = loadProjectNameMethod.getResult();
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}
	
	public boolean isSuperuser() throws UniformAppletException {
		IsSuperuserMethod method = new IsSuperuserMethod(new Caseless(SystemContext.instance().getUserId()));
		initiate(method);
		return method.getResult().booleanValue();
	}
	
	public List<Caseless> findFilesByProject(Caseless projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		Integer projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null) {
			throw new UniformAppletException("Project does not exists!");
		}
		FindFilesByProjectMethod method = new FindFilesByProjectMethod(
				projectIdentifier, userId);
		initiate(method);
		List<Integer> fileIdentifiers = method.getResult();

		List<Caseless> fileNames = new ArrayList<Caseless>();
		for (Integer id : fileIdentifiers) {
			FileIdentifier identifier = cache.getFileForIdentifier(id);
			if (identifier == null) {
				LoadFileNameMethod loadFileNameMethod = new LoadFileNameMethod(
						id, userId);
				initiate(loadFileNameMethod);
				Caseless name = loadFileNameMethod.getResult();
				cache.cacheItem(projectName, name, id);
				identifier = cache.getFileForIdentifier(id);
			}
			fileNames.add(identifier.getFileName());
		}
		return fileNames;
	}

	public boolean existsFile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		/* return invoker.existsFile(projectIdentifier, fileName); */
		return cache.containsIdentifierFor(projectName, fileName);
	}

	public boolean existsProject(Caseless projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		/*
		 * Integer projectIdentifier = cache.getIdentifierFor(projectName);
		 * if(projectIdentifier == null) return false; return
		 * invoker.existsProject(projectIdentifier);
		 */
		return cache.containsIdentifierFor(projectName);
	}

	public void deleteFile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName, fileName);
		DeleteFileMethod method = new DeleteFileMethod(fileIdentifier, userId);
		initiate(method);
	}

	public void deleteProject(Caseless projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName);
		DeleteProjectMethod method = new DeleteProjectMethod(fileIdentifier, userId);
		initiate(method);
	}

	public void createProject(Caseless projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		CreateProjectMethod method = new CreateProjectMethod(projectName, userId);
		initiate(method);
		Integer projectIdentifier = method.getResult();
		cache.cacheItem(projectName, projectIdentifier);
	}

	public void createFile(Caseless projectName, Caseless fileName, FileType type,
			String data) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (type == null)
			throw new NullPointerException("File type can not be null.");
		Integer projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		CreateFileMethod method = new CreateFileMethod(projectIdentifier,
				fileName, type, data, userId);
		initiate(method);

		Integer fileIdentifier = method.getResult();
		cache.cacheItem(projectName, fileName, fileIdentifier);
	}

	public void saveFile(Caseless projectName, Caseless fileName, String content)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (content == null)
			throw new NullPointerException("File content can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		SaveFileMethod method = new SaveFileMethod(fileIdentifier, content, userId);
		initiate(method);
	}

	public String loadFileContent(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
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

	public String loadPredefinedFileContent(Caseless fileName)
			throws UniformAppletException {
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		LoadPredefinedFileContentMethod method = new LoadPredefinedFileContentMethod(
				fileName, userId);
		initiate(method);
		return method.getResult();
	}

	public FileType loadFileType(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null) {
			return FileType.PREDEFINED;
			// throw new UniformAppletException("File does not exists!");
		}
		FileType fileType = cache.getFileType(fileIdentifier);
		if (fileType == null) {
			LoadFileTypeMethod method = new LoadFileTypeMethod(fileIdentifier, userId);
			initiate(method);
			fileType = method.getResult();
			cache.cacheFileType(fileIdentifier, fileType);
		}
		return fileType;
	}

	public Hierarchy extractHierarchy(Caseless projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Integer projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");

		ExtractHierarchyMethod hierarhyMethod = new ExtractHierarchyMethod(
				projectIdentifier, userId);
		initiate(hierarhyMethod);

		Hierarchy h = hierarhyMethod.getResult();
		for (Caseless fileName : getAllForHierarchy(h)) {
			Integer id = cache.getIdentifierFor(projectName, fileName);
			if (id == null) {
				ExistsFile2Method existsMethod = new ExistsFile2Method(
						projectIdentifier, fileName, userId);
				initiate(existsMethod);
				boolean exists = existsMethod.getResult().booleanValue();
				if (exists) {
					FindFileByNameMethod findMethod = new FindFileByNameMethod(
							projectIdentifier, fileName, userId);
					initiate(findMethod);
					Integer fileIdentifier = findMethod.getResult();
					cache.cacheItem(projectName, fileName, fileIdentifier);
				} else {
					// invoker.
				}
			}
		}
		return h;
	}
	
	private Set<Caseless> getAllForHierarchy(Hierarchy h) {
	    Set<Caseless> names = new HashSet<Caseless>(h.fileCount());
	    for (Caseless f : h.getTopLevelFiles()) {
	        names.add(f);
	        names.addAll(h.getAllDependenciesForFile(f));
        }
	    return names;
	}

	public VHDLGenerationResult generateVHDL(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		GenerateVHDLMethod method = new GenerateVHDLMethod(fileIdentifier, userId);
		initiate(method);
		return method.getResult();
	}

	public CompilationResult compile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		CompileFileMethod method = new CompileFileMethod(fileIdentifier, userId);
		initiate(method);
		return method.getResult();
	}

	public SimulationResult runSimulation(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		SimulateFileMethod method = new SimulateFileMethod(fileIdentifier, userId);
		initiate(method);
		return method.getResult();
	}

	public CircuitInterface getCircuitInterfaceFor(Caseless projectName,
	        Caseless fileName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Integer fileIdentifier = cache.getIdentifierFor(projectName, fileName);
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
		List<Integer> userFileIds = findMethod.getResult();
		for (Integer id : userFileIds) {
			LoadUserFileNameMethod nameMethod = new LoadUserFileNameMethod(id, userId);
			initiate(nameMethod);
			Caseless name = nameMethod.getResult();
			cache.cacheUserFileItem(name, id);
			LoadUserFileContentMethod contentMethod = new LoadUserFileContentMethod(
					id, userId);
			initiate(contentMethod);
			String data = contentMethod.getResult();
			properties.setProperty(name.toString(), data);
		}
		UserPreferences.instance().init(properties);
	}

	private void initiate(Method<? extends Serializable> method)
			throws UniformAppletException {
		initiator.initiateCall(method);
	}

}