package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.FileIdentifier;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

	public class Cache {

		private MethodInvoker invoker;

		private String ownerId;
		private List<String> filetypes;
		private Properties editors;
		private Properties views;
		
		private Map<String, Long> identifiers;
		
		private List<Long> compilationHistory;
		private List<Long> simulationHistory;
		
		public Cache(MethodInvoker invoker, String ownerId) {
			if(invoker == null) throw new NullPointerException("Method invoker can not be null.");
			if(ownerId == null) throw new NullPointerException("Owner identifier can not be null.");
			identifiers = new HashMap<String, Long>();
			compilationHistory = new ArrayList<Long>();
			simulationHistory = new ArrayList<Long>();
			filetypes = loadType("filetypes.properties");
			editors = loadResource("editors.properties");
			views = loadResource("views.properties");
			this.invoker = invoker;
			this.ownerId = ownerId;
		}
		
		public List<String> findProjects() throws UniformAppletException {
			List<Long> projectIdentifiers = invoker.findProjectsByUser(ownerId);

			List<String> projectNames = new ArrayList<String>();
			for(Long id : projectIdentifiers) {
				String name = invoker.loadProjectName(id);
				cacheItem(name, id);
				projectNames.add(name);
			}
			return projectNames;
		}
		
		public List<String> findFilesByProject(String projectName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			Long projectIdentifier = getIdentifierFor(projectName);
			if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
			List<Long> fileIdentifiers =  invoker.findFileByProject(projectIdentifier);
			
			List<String> fileNames = new ArrayList<String>();
			for(Long id : fileIdentifiers) {
				String name = invoker.loadFileName(id);
				cacheItem(projectName, name, id);
				fileNames.add(name);
			}
			return fileNames;
		}
		
		public boolean existsFile(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long projectIdentifier = getIdentifierFor(projectName);
			if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
			return invoker.existsFile(projectIdentifier, fileName);
		}
		
		public boolean existsProject(String projectName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			Long projectIdentifier = getIdentifierFor(projectName);
			if(projectIdentifier == null) return false;
			return invoker.existsProject(projectIdentifier);
		}
		
		public void createProject(String projectName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			Long identifier = invoker.createProject(projectName, ownerId);
			cacheItem(projectName, identifier);
		}

		public void createFile(String projectName, String fileName, String type) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			if(type == null) throw new NullPointerException("File type can not be null.");
			Long projectIdentifier = getIdentifierFor(projectName);
			if(projectIdentifier == null) throw new UniformAppletException("Project does not exists!");
			Long fileIdentifier = invoker.createFile(projectIdentifier, fileName, type);
			cacheItem(projectName, fileName, fileIdentifier);
		}
		
		public void saveFile(String projectName, String fileName, String content) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			if(content == null) throw new NullPointerException("File content can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			invoker.saveFile(fileIdentifier, content);
		}
		
		public String loadFileContent(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			return invoker.loadFileContent(fileIdentifier);
		}
		
		public String loadFileType(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			return invoker.loadFileType(fileIdentifier);
		}
		
		public FileIdentifier getLastCompilationHistoryTarget() throws UniformAppletException {
			if(compilationHistory.isEmpty()) throw new UniformAppletException("No history of compilation.");
			Long last = compilationHistory.get(compilationHistory.size() - 1);
			return getFileIdentifierFor(last);
		}
		
		public CompilationResult compile(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			if(compilationHistory.contains(fileIdentifier)) {
				compilationHistory.remove(fileIdentifier);
			}
			compilationHistory.add(fileIdentifier);
			return invoker.compileFile(fileIdentifier);
		}
		
		public List<FileIdentifier> getCompilationHistory() {
			List<FileIdentifier> history = new ArrayList<FileIdentifier>();
			for(Long id : compilationHistory) {
				history.add(getFileIdentifierFor(id));
			}
			return history;
		}
		
		public boolean compilationHistoryIsEmpty() {
			return compilationHistory.isEmpty();
		}
		
		public FileIdentifier getLastSimulationHistoryTarget() throws UniformAppletException {
			if(simulationHistory.isEmpty()) throw new UniformAppletException("No history of simulation.");
			Long last = simulationHistory.get(simulationHistory.size() - 1);
			return getFileIdentifierFor(last);
		}


		public SimulationResult runSimulation(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			if(simulationHistory.contains(fileIdentifier)) {
				simulationHistory.remove(fileIdentifier);
			}
			simulationHistory.add(fileIdentifier);
			return invoker.runSimulation(fileIdentifier);
		}
		
		public List<FileIdentifier> getSimulationHistory() {
			List<FileIdentifier> history = new ArrayList<FileIdentifier>();
			for(Long id : simulationHistory) {
				history.add(getFileIdentifierFor(id));
			}
			return history;
		}
		
		public boolean simulationHistoryIsEmpty() {
			return simulationHistory.isEmpty();
		}
		
		public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException {
			if(projectName == null) throw new NullPointerException("Project name can not be null.");
			if(fileName == null) throw new NullPointerException("File name can not be null.");
			Long fileIdentifier = getIdentifierFor(projectName, fileName);
			if(fileIdentifier == null) throw new UniformAppletException("File does not exists!");
			return invoker.extractCircuitInterface(fileIdentifier);
		}
		
		public IEditor getEditor(String type) throws UniformAppletException {
			if(type == null) throw new NullPointerException("Type can not be null.");
			String editorName = editors.getProperty(type);
			if(editorName == null) throw new IllegalArgumentException("Can not find editor for given type.");
			IEditor editor = null;
			try {
				editor = (IEditor)Class.forName(editorName).newInstance();
			} catch (Exception e) {
				throw new UniformAppletException("Can not instantiate editor.");
			}
			return editor;
		}
		
		public IView getView(String type) throws UniformAppletException {
			if(type == null) throw new NullPointerException("Type can not be null.");
			String viewName = views.getProperty(type);
			if(viewName == null) throw new IllegalArgumentException("Can not find view for given type.");
			IView view = null;
			try {
				view = (IView)Class.forName(viewName).newInstance();
			} catch (Exception e) {
				throw new UniformAppletException("Can not instantiate view.");
			}
			return view;
		}
		
		public String getUserFile(String type) throws UniformAppletException {
			List<Long> userFiles = invoker.findUserFilesByOwner(ownerId);
			
			for(Long id : userFiles) {
				String userFileType = invoker.loadUserFileType(id);
				if(type.equals(userFileType)) {
					return invoker.loadUserFileContent(id);
				}
			}
			throw new UniformAppletException("No such user file.");
		}
		
		private List<String> loadType(String file) {
			List<String> types = new ArrayList<String>();
			InputStream in = this.getClass().getResourceAsStream(file);
			if(in == null) throw new NullPointerException("Can not find resource " + file + ".");
			Properties p = new Properties();
			try {
				p.load(in);
				for(Object o : p.keySet()) {
					String key = (String) o;
					types.add(p.getProperty(key));
				}
			} catch (IOException e) {
				return null;
			} finally {
				try {in.close();} catch (Throwable ignore) {}
			}
			return types;
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
		
		private FileIdentifier getFileIdentifierFor(Long fileIdentifier) {
			for(Entry<String, Long> entry : identifiers.entrySet()) {
				if(entry.getValue().equals(fileIdentifier)) {
					String key = entry.getKey();
					String[] names = key.split("[|]");
					if(names.length != 2) throw new IllegalArgumentException("Found project insted of a file.");
					FileIdentifier file = new FileIdentifier(names[0], names[1]);
					return file;
				}
			}
			return null;
		}
		
		private Long getIdentifierFor(String projectName) {
			String key = makeKey(projectName);
			return identifiers.get(key);
		}

		private Long getIdentifierFor(String projectName, String fileName) {
			String key = makeKey(projectName, fileName);
			return identifiers.get(key);
		}
		
		private void cacheItem(String projectName, Long projectIdentifier) {
			String key = makeKey(projectName);
			identifiers.put(key, projectIdentifier);
		}
		
		private void cacheItem(String projectName, String fileName, Long fileIdentifier) {
			String key = makeKey(projectName, fileName);
			identifiers.put(key, fileIdentifier);
		}
		
		private String makeKey(String str) {
			return str;
		}

		private String makeKey(String str1, String str2) {
			return str1 + "|" + str2;
		}

	}