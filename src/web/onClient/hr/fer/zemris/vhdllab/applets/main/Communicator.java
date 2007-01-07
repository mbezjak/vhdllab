package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.Preferences;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.ArrayList;
import java.util.List;

public class Communicator {

	private String ownerId;
	private MethodInvoker invoker;
	private Cache cache;

	public Communicator(MethodInvoker invoker, String ownerId) {
		if(invoker == null) throw new NullPointerException("Method invoker can not be null.");
		if(ownerId == null) throw new NullPointerException("Owner identifier can not be null.");
		cache = new Cache();
		this.invoker = invoker;
		this.ownerId = ownerId;
	}
	
	public void cleanUp() throws UniformAppletException {
		for(Preferences pref : cache.getAllPreferences()) {
			String data = pref.serialize();
			Long id = cache.getIdentifierFor(pref);
			invoker.saveUserFile(id, data);
		}
	}

	public List<String> getFileTypes() {
		return cache.getFileTypes();
	}

	public List<String> getAllProjects() throws UniformAppletException {
		List<Long> projectIdentifiers = invoker.findProjectsByUser(ownerId);
		
		List<String> projectNames = new ArrayList<String>();
		for(Long id : projectIdentifiers) {
			String projectName = cache.getProjectForIdentifier(id);
			if(projectName == null) {
				String name = invoker.loadProjectName(id);
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}

	public List<String> findFilesByProject(String projectName) throws UniformAppletException {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if(projectIdentifier == null) {
			throw new UniformAppletException("Project does not exists!");
		}
		List<Long> fileIdentifiers =  invoker.findFileByProject(projectIdentifier);

		List<String> fileNames = new ArrayList<String>();
		for(Long id : fileIdentifiers) {
			FileIdentifier identifier = cache.getFileForIdentifier(id);
			if(identifier == null) {
				String name = invoker.loadFileName(id);
				cache.cacheItem(projectName, name, id);
				identifier = cache.getFileForIdentifier(id);
			}
			fileNames.add(identifier.getFileName());
		}
		return fileNames;
	}

	public boolean existsFile(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
		/*return invoker.existsFile(projectIdentifier, fileName);*/
		return cache.containsIdentifierFor(projectName, fileName);
	}

	public boolean existsProject(String projectName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		/*Long projectIdentifier = cache.getIdentifierFor(projectName);
		if(projectIdentifier == null) return false;
		return invoker.existsProject(projectIdentifier);*/
		return cache.containsIdentifierFor(projectName);
	}
	
	public void deleteFile(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName, fileName);
		invoker.deleteFile(fileIdentifier);
	}
	
	public void deleteProject(String projectName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName);
		invoker.deleteProject(fileIdentifier);
	}

	public void createProject(String projectName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		Long projectIdentifier = invoker.createProject(projectName, ownerId);
		cache.cacheItem(projectName, projectIdentifier);
	}

	public void createFile(String projectName, String fileName, String type) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		if(type == null) throw new NullPointerException("File type can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
		Long fileIdentifier = invoker.createFile(projectIdentifier, fileName, type);
		cache.cacheItem(projectName, fileName, fileIdentifier);
	}

	public void saveFile(String projectName, String fileName, String content) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		if(content == null) throw new NullPointerException("File content can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		invoker.saveFile(fileIdentifier, content);
	}

	public String loadFileContent(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		return invoker.loadFileContent(fileIdentifier);
	}

	public String loadFileType(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		return invoker.loadFileType(fileIdentifier);
	}

	public Hierarchy extractHierarchy(String projectName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
		return invoker.extractHierarchy(projectIdentifier);
	}

	public String generateVHDL(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		return invoker.generateVHDL(fileIdentifier);
	}

	public FileIdentifier getLastCompilationHistoryTarget() {
		return cache.getLastCompilationHistoryTarget();
	}

	public CompilationResult compile(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		cache.cacheCompilationTargetToHistory(projectName, fileName);
		return invoker.compileFile(fileIdentifier);
	}

	public List<FileIdentifier> getCompilationHistory() {
		return cache.getCompilationHistory();
	}

	public boolean compilationHistoryIsEmpty() {
		return cache.compilationHistoryIsEmpty();
	}

	public FileIdentifier getLastSimulationHistoryTarget() {
		return cache.getLastSimulationHistoryTarget();
	}


	public SimulationResult runSimulation(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		cache.cacheSimulationTargetToHistory(projectName, fileName);
		return invoker.runSimulation(fileIdentifier);
	}

	public List<FileIdentifier> getSimulationHistory() {
		return cache.getSimulationHistory();
	}

	public boolean simulationHistoryIsEmpty() {
		return cache.simulationHistoryIsEmpty();
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
		return invoker.extractCircuitInterface(fileIdentifier);
	}

	public IEditor getEditor(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		return cache.getEditor(type);
	}

	public IView getView(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		return cache.getView(type);
	}

	public String getViewType(IView view) {
		if(view == null) {
			throw new NullPointerException("Type can not be null.");
		}
		return cache.getViewType(view);
	}

	public void savePreferences(Preferences pref) throws UniformAppletException {
		if(pref == null) {
			throw new NullPointerException("Preferences can not be null.");
		}
		cache.recachePreferences(pref);
	}

	public Preferences getPreferences(String type) throws UniformAppletException {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		Long userFileIdentifier = cache.getIdentifierForUserFile(type);
		if(userFileIdentifier == null) {
			List<Long> userFiles = invoker.findUserFilesByOwner(ownerId);
			for(Long id : userFiles) {
				String userFileType = invoker.loadUserFileType(id);
				if(type.equals(userFileType)) {
					cache.cacheUserFileItem(userFileType, id);
					userFileIdentifier = id;
					break;
				}
			}
			if(userFileIdentifier == null) {
				throw new IllegalArgumentException("Can not find preferences for type: " + type);
			}
		}
		Preferences pref = cache.getPreferences(userFileIdentifier);
		if(pref == null) {
			String data = invoker.loadUserFileContent(userFileIdentifier);
			Preferences p = Preferences.deserialize(data);
			cache.cachePreferences(userFileIdentifier, p);
			pref = p;
		}
		return pref;
	}

	public List<Preferences> getAllPreferences() throws UniformAppletException {
		List<Long> userFiles = invoker.findUserFilesByOwner(ownerId);
		
		List<Preferences> preferences = new ArrayList<Preferences>();
		for(Long id : userFiles) {
			if(!cache.containsPreferencesByIdentifier(id)) {
				String type = invoker.loadUserFileType(id);
				cache.cacheUserFileItem(type, id);
				String data = invoker.loadUserFileContent(id);
				Preferences pref = Preferences.deserialize(data);
				cache.cachePreferences(id, pref);
			}
			Preferences pref = cache.getPreferences(id);
			preferences.add(pref);
		}
		return preferences;
	}

}