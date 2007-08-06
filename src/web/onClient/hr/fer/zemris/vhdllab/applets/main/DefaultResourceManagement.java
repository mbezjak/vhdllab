package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.EventListenerList;

/**
 * This is a default implementation of {@link IResourceManagement} interface. It
 * uses {@link Communicator} as a resources provider.
 * 
 * @author Miro Bezjak
 */
public class DefaultResourceManagement implements IResourceManagement {

	/**
	 * A resource provider.
	 */
	private Communicator communicator;

	/**
	 * Vetoable resource listeners.
	 */
	private EventListenerList listeners;

	/**
	 * Constructs a default resource menagement using <code>communicator</code>
	 * as a resource provider.
	 * 
	 * @param communicator
	 *            a communicator as resource provider
	 * @throws NullPointerException
	 *             if <code>communicator</code> is <code>null</code>
	 */
	public DefaultResourceManagement(Communicator communicator) {
		super();
		if (communicator == null) {
			throw new NullPointerException("Communicator cant be null");
		}
		this.listeners = new EventListenerList();
		this.communicator = communicator;
	}

	/* LISTENERS METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#addVetoableResourceListener(hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener)
	 */
	@Override
	public void addVetoableResourceListener(VetoableResourceListener l) {
		listeners.add(VetoableResourceListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#removeVetoableResourceListener(hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener)
	 */
	@Override
	public void removeVetoableResourceListener(VetoableResourceListener l) {
		listeners.remove(VetoableResourceListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#removeAllVetoableResourceListeners()
	 */
	@Override
	public void removeAllVetoableResourceListeners() {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			listeners.remove(VetoableResourceListener.class, l);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getVetoableResourceListeners()
	 */
	@Override
	public VetoableResourceListener[] getVetoableResourceListeners() {
		return listeners.getListeners(VetoableResourceListener.class);
	}

	/* RESOURCE MANIPULATION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#createNewResource(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean createNewResource(String projectName, String fileName,
			String type) throws UniformAppletException {
		return createNewResource(projectName, fileName, type, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#createNewResource(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createNewResource(String projectName, String fileName,
			String type, String data) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File type cant be null");
		}
		try {
			fireBeforeResourceCreation(projectName, fileName, type);
		} catch (ResourceVetoException e) {
			return false;
		}
		communicator.createFile(projectName, fileName, type, data);
		try {
			fireResourceCreated(projectName, fileName, type);
		} catch (ResourceVetoException e) {
			// rollback
			deleteFile(projectName, fileName);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#createNewProject(java.lang.String)
	 */
	@Override
	public boolean createNewProject(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		try {
			fireBeforeProjectCreation(projectName);
		} catch (ResourceVetoException e) {
			return false;
		}
		communicator.createProject(projectName);
		try {
			fireProjectCreated(projectName);
		} catch (ResourceVetoException e) {
			// rollback
			deleteProject(projectName);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#deleteFile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void deleteFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		try {
			fireBeforeResourceDeletion(projectName, fileName);
		} catch (ResourceVetoException e) {
			return;
		}
		communicator.deleteFile(projectName, fileName);
		fireResourceDeleted(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#deleteProject(java.lang.String)
	 */
	@Override
	public void deleteProject(String projectName) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		try {
			fireBeforeProjectDeletion(projectName);
		} catch (ResourceVetoException e) {
			return;
		}
		communicator.deleteProject(projectName);
		fireProjectDeleted(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#existsFile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean existsFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.existsFile(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#existsProject(java.lang.String)
	 */
	@Override
	public boolean existsProject(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.existsProject(projectName);
	}

	/* DATA EXTRACTION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getAllProjects()
	 */
	@Override
	public List<String> getAllProjects() throws UniformAppletException {
		return communicator.getAllProjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getFilesFor(java.lang.String)
	 */
	@Override
	public List<String> getFilesFor(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.findFilesByProject(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getAllCircuits(java.lang.String)
	 */
	@Override
	public List<String> getAllCircuits(String projectName)
			throws UniformAppletException {
		List<String> fileNames = getFilesFor(projectName);
		List<String> circuits = new ArrayList<String>();
		for (String name : fileNames) {
			if (isCircuit(projectName, name)) {
				circuits.add(name);
			}
		}
		return circuits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getAllTestbenches(java.lang.String)
	 */
	@Override
	public List<String> getAllTestbenches(String projectName)
			throws UniformAppletException {
		List<String> fileNames = getFilesFor(projectName);
		List<String> testbenches = new ArrayList<String>();
		for (String name : fileNames) {
			if (isTestbench(projectName, name)) {
				testbenches.add(name);
			}
		}
		return testbenches;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getCircuitInterfaceFor(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public CircuitInterface getCircuitInterfaceFor(String projectName,
			String fileName) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.getCircuitInterfaceFor(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#generateVHDL(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String generateVHDL(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.generateVHDL(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getFileContent(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getFileContent(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.loadFileContent(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getPredefinedFileContent(java.lang.String)
	 */
	@Override
	public String getPredefinedFileContent(String fileName)
			throws UniformAppletException {
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.loadPredefinedFileContent(fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#saveFile(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void saveFile(String projectName, String fileName, String content)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		if (content == null) {
			throw new NullPointerException("File content cant be null");
		}
		communicator.saveFile(projectName, fileName, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getFileType(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getFileType(String projectName, String fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		try {
			return communicator.loadFileType(projectName, fileName);
		} catch (UniformAppletException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#extractHierarchy(java.lang.String)
	 */
	@Override
	public Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.extractHierarchy(projectName);
	}

	/* COMPILATION METHOD */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#compile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException {
		if (!isCompilable(projectName, fileName)) {
			return null;
		}
		try {
			fireBeforeResourceCompilation(projectName, fileName);
		} catch (ResourceVetoException e) {
			return null;
		}
		CompilationResult result = communicator.compile(projectName, fileName);
		fireResourceCompiled(projectName, fileName, result);
		return result;
	}

	/* SIMULATION METHOD */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#simulate(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public SimulationResult simulate(String projectName, String fileName)
			throws UniformAppletException {
		if (!isSimulatable(projectName, fileName)) {
			return null;
		}
		try {
			fireBeforeResourceSimulation(projectName, fileName);
		} catch (ResourceVetoException e) {
			return null;
		}
		SimulationResult result = communicator.runSimulation(projectName,
				fileName);
		fireResourceSimulated(projectName, fileName, result);
		return result;
	}

	/* IS-SOMETHING METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isCircuit(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCircuit(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isCircuit(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isTestbench(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isTestbench(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		if (FileTypes.isTestbench(type)) {
			return true;
		}

		CircuitInterface ci;
		try {
			ci = getCircuitInterfaceFor(projectName, fileName);
		} catch (UniformAppletException e) {
			return false;
		}
		if (ci.getPorts().isEmpty()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isSimulation(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulation(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isSimulation(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isCompilable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCompilable(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isCircuit(type) && !FileTypes.isPredefined(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isSimulatable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulatable(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isTestbench(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isCorrectEntityName(java.lang.String)
	 */
	@Override
	public boolean isCorrectEntityName(String name) {
		return StringFormat.isCorrectEntityName(name);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isCorrectFileName(java.lang.String)
	 */
	@Override
	public boolean isCorrectFileName(String name) {
		return StringFormat.isCorrectFileName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#isCorrectProjectName(java.lang.String)
	 */
	@Override
	public boolean isCorrectProjectName(String name) {
		return StringFormat.isCorrectProjectName(name);
	}

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getPreferences()
	 */
	@Override
	public IUserPreferences getPreferences() {
		return communicator.getPreferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getResourceBundle(java.lang.String)
	 */
	@Override
	public ResourceBundle getResourceBundle(String baseName) {
		String language;
		try {
			language = getPreferences().getProperty(
					UserFileConstants.COMMON_LANGUAGE);
		} catch (PropertyAccessException e) {
			language = null;
		}
		if (language == null) {
			language = "en";
		}

		ResourceBundle bundle = CachedResourceBundles.getBundle(baseName,
				language);
		if (bundle == null) {
			throw new IllegalArgumentException("No bundle found.");
		}
		return bundle;
	}

	/* FIRE EVENTS METHODS */

	/**
	 * Fires beforeProjectCreation event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeProjectCreation(String projectName)
			throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeProjectCreation(projectName);
		}
	}

	/**
	 * Fires projectCreated event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireProjectCreated(String projectName)
			throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.projectCreated(projectName);
		}
	}

	/**
	 * Fires beforeProjectDeletion event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeProjectDeletion(String projectName)
			throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeProjectDeletion(projectName);
		}
	}

	/**
	 * Fires projectDeleted event.
	 * 
	 * @param projectName
	 *            a name of a project
	 */
	private void fireProjectDeleted(String projectName) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.projectDeleted(projectName);
		}
	}

	/**
	 * Fires beforeResourceCreation event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @param type
	 *            a file type
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeResourceCreation(String projectName,
			String fileName, String type) throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeResourceCreation(projectName, fileName, type);
		}
	}

	/**
	 * Fires resourceCreated event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @param type
	 *            a file type
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireResourceCreated(String projectName, String fileName,
			String type) throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceCreated(projectName, fileName, type);
		}
	}

	/**
	 * Fires beforeResourceDeletion event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeResourceDeletion(String projectName, String fileName)
			throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeResourceDeletion(projectName, fileName);
		}
	}

	/**
	 * Fires resourceDeleted event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 */
	private void fireResourceDeleted(String projectName, String fileName) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceDeleted(projectName, fileName);
		}
	}

	/**
	 * Fires beforeResourceCompilation event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeResourceCompilation(String projectName,
			String fileName) throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeResourceCompilation(projectName, fileName);
		}
	}

	/**
	 * Fires resourceCompiled event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @param result
	 *            a compilation result
	 */
	private void fireResourceCompiled(String projectName, String fileName,
			CompilationResult result) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceCompiled(projectName, fileName, result);
		}
	}

	/**
	 * Fires beforeResourceSimulation event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @throws ResourceVetoException
	 *             a veto
	 */
	private void fireBeforeResourceSimulation(String projectName,
			String fileName) throws ResourceVetoException {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.beforeResourceSimulation(projectName, fileName);
		}
	}

	/**
	 * Fires resourceSimulated event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @param result
	 *            a compilation result
	 */
	private void fireResourceSimulated(String projectName, String fileName,
			SimulationResult result) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceSimulated(projectName, fileName, result);
		}
	}

}
