package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class Cache {

	private Properties editors;
	private Properties views;

	private Map<String, IView> cachedViews;

	private Map<FileIdentifier, Long> identifiers;
	private Map<String, Long> userFileIdentifiers;

	private IUserPreferences preferences;
	private List<FileIdentifier> compilationHistory;
	private List<FileIdentifier> simulationHistory;


	public Cache() {
		cachedViews = new HashMap<String, IView>();
		identifiers = new HashMap<FileIdentifier, Long>();
		userFileIdentifiers = new HashMap<String, Long>();
		compilationHistory = new LinkedList<FileIdentifier>();
		simulationHistory = new LinkedList<FileIdentifier>();

		editors = loadResource("editors.properties");
		if(editors == null) throw new IllegalStateException("Cant load editors.");
		views = loadResource("views.properties");
		if(views == null) throw new IllegalStateException("Cant load views.");
	}

	public FileIdentifier getLastCompilationHistoryTarget() {
		if(compilationHistoryIsEmpty()) return null;
		FileIdentifier last = compilationHistory.get(compilationHistory.size() - 1);
		return last;
	}

	public List<FileIdentifier> getCompilationHistory() {
		return Collections.unmodifiableList(compilationHistory);
	}

	public boolean compilationHistoryIsEmpty() {
		return compilationHistory.isEmpty();
	}

	public void cacheCompilationTargetToHistory(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		if(compilationHistory.contains(key)) {
			compilationHistory.remove(key);
		}
		compilationHistory.add(key);
	}

	public FileIdentifier getLastSimulationHistoryTarget() {
		if(simulationHistoryIsEmpty()) return null;
		FileIdentifier last = simulationHistory.get(simulationHistory.size() - 1);
		return last;
	}

	public List<FileIdentifier> getSimulationHistory() {
		return Collections.unmodifiableList(simulationHistory);
	}

	public boolean simulationHistoryIsEmpty() {
		return simulationHistory.isEmpty();
	}

	public void cacheSimulationTargetToHistory(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		if(simulationHistory.contains(key)) {
			simulationHistory.remove(key);
		}
		simulationHistory.add(key);
	}

	public IEditor getEditor(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		String editorName = editors.getProperty(type);
		if(editorName == null) {
			throw new IllegalArgumentException("Can not find editor for given type.");
		}
		IEditor editor = null;
		try {
			editor = (IEditor)Class.forName(editorName).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Can not instantiate editor.");
		}
		return editor;
	}

	public IView getView(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		IView view = cachedViews.get(type);
		if(view == null) {
			String viewName = views.getProperty(type);
			if(viewName == null) {
				throw new IllegalArgumentException("Can not find view for given type.");
			}
			try {
				view = (IView)Class.forName(viewName).newInstance();
			} catch (Exception e) {
				throw new IllegalStateException("Can not instantiate view.");
			}
			cacheView(type, view);
		}
		return view;
	}

	public String getViewType(IView view) {
		if(view == null) {
			throw new NullPointerException("Type can not be null.");
		}
		for(Entry<String, IView> entry : cachedViews.entrySet()) {
			if(entry.getValue() == view) {
				return entry.getKey();
			}
		}
		throw new IllegalArgumentException("Unknown view!");
	}

	private void cacheView(String type, IView view) {
		cachedViews.put(type, view);
	}

	public boolean containsIdentifierFor(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		return getIdentifierFor(projectName) != null;
	}

	public boolean containsIdentifierFor(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		return getIdentifierFor(projectName, fileName) != null;
	}
	
	public List<String> findFilesForProject(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		List<String> files = new ArrayList<String>();
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isFile() && identifier.getProjectName().equals(projectName)) {
				files.add(identifier.getFileName());
			}
		}
		return files;
	}
	
	public List<String> findFilesForProject() {
		List<String> projects = new ArrayList<String>();
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(identifier.isProject()) {
				projects.add(identifier.getProjectName());
			}
		}
		return projects;
	}
	
	public IUserPreferences getUserPreferences() {
		return preferences;
	}
	
	public Long getIdentifierForProperty(String name) {
		return userFileIdentifiers.get(name);
	}

	public FileIdentifier getFileForIdentifier(Long id) {
		if(id == null) {
			throw new NullPointerException("File identifier can not be null.");
		}
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isFile()) {
				return identifier;
			}
		}
		return null;
	}
	
	public String getProjectForIdentifier(Long id) {
		if(id == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		for(Entry<FileIdentifier, Long> entry : identifiers.entrySet()) {
			FileIdentifier identifier = entry.getKey();
			if(entry.getValue().equals(id) && identifier.isProject()) {
				return identifier.getProjectName();
			}
		}
		return null;
	}
	
	public Long getIdentifierFor(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		return identifiers.get(key);
	}

	public Long getIdentifierFor(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		return identifiers.get(key);
	}
	
	public void cacheUserPreferences(IUserPreferences preferences) {
		this.preferences = preferences;
	}

	public void cacheUserFileItem(String name, Long userFileIdentifier) {
		if(name == null) {
			throw new NullPointerException("Name can not be null.");
		}
		if(userFileIdentifier == null) {
			throw new NullPointerException("User file identifier can not be null.");
		}
		userFileIdentifiers.put(name, userFileIdentifier);
	}
	
	public void cacheItem(String projectName, Long projectIdentifier) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(projectIdentifier == null) {
			throw new NullPointerException("Project identifier can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.put(key, projectIdentifier);
	}

	public void cacheItem(String projectName, String fileName, Long fileIdentifier) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(fileIdentifier == null) {
			throw new NullPointerException("File identifier can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		identifiers.put(key, fileIdentifier);
	}

	public void removeUserFileItem(String type) {
		if(type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		userFileIdentifiers.remove(type);
	}

	public void removeItem(String projectName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName);
		identifiers.remove(key);
	}

	public void removeItem(String projectName, String fileName) {
		if(projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if(fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		FileIdentifier key = makeIdentifier(projectName, fileName);
		compilationHistory.remove(key);
		simulationHistory.remove(key);
		identifiers.remove(key);
	}

	private FileIdentifier makeIdentifier(String projectName) {
		FileIdentifier identifier = new FileIdentifier(projectName);
		return identifier;
	}

	private FileIdentifier makeIdentifier(String projectName, String fileName) {
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return identifier;
	}

	private Properties loadResource(String file) {
		InputStream in = this.getClass().getResourceAsStream(file);
		if(in == null) throw new NullPointerException("Can not find resource " + file + ".");
		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			return null;
		} finally {
			try {in.close();} catch (Throwable ignore) {}
		}
		return p;
	}

}