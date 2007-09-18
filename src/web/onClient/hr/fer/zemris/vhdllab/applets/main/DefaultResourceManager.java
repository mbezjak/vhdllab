package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.utilities.StringFormat;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

/**
 * This is a default implementation of {@link IResourceManager} interface. It
 * uses {@link Communicator} as a resources provider.
 * 
 * @author Miro Bezjak
 */
public class DefaultResourceManager implements IResourceManager {

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
	public DefaultResourceManager(Communicator communicator) {
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#addVetoableResourceListener(hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener)
	 */
	@Override
	public void addVetoableResourceListener(VetoableResourceListener l) {
		listeners.add(VetoableResourceListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#removeVetoableResourceListener(hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener)
	 */
	@Override
	public void removeVetoableResourceListener(VetoableResourceListener l) {
		listeners.remove(VetoableResourceListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#removeAllVetoableResourceListeners()
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getVetoableResourceListeners()
	 */
	@Override
	public VetoableResourceListener[] getVetoableResourceListeners() {
		return listeners.getListeners(VetoableResourceListener.class);
	}

	/* RESOURCE MANIPULATION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#createNewResource(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#createNewResource(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#createNewProject(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#deleteFile(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#deleteProject(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#existsFile(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#existsProject(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getAllProjects()
	 */
	@Override
	public List<String> getAllProjects() throws UniformAppletException {
		return communicator.getAllProjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getFilesFor(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getAllCircuits(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getAllTestbenches(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getCircuitInterfaceFor(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#generateVHDL(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public VHDLGenerationResult generateVHDL(String projectName, String fileName)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getFileContent(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getPredefinedFileContent(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#saveFile(java.lang.String,
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
		fireResourceSaved(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#saveErrorMessage(java.lang.String)
	 */
	@Override
	public void saveErrorMessage(String content) throws UniformAppletException {
		if (content == null) {
			throw new NullPointerException("Content cant be null");
		}
		communicator.saveErrorMessage(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getFileType(java.lang.String,
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#extractHierarchy(java.lang.String)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#compile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException {
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#simulate(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public SimulationResult simulate(String projectName, String fileName)
			throws UniformAppletException {
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCircuit(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCircuit(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isCircuit(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isTestbench(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isTestbench(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isSimulation(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulation(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isSimulation(type);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCompilable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCompilable(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isCircuit(type) && !FileTypes.isPredefined(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isSimulatable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulatable(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		String type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileTypes.isTestbench(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#canGenerateVHDLCode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean canGenerateVHDLCode(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		return isCircuit(projectName, fileName)
				|| isTestbench(projectName, fileName);
		// TODO ovdje dodat i isPredefined
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectEntityName(java.lang.String)
	 */
	@Override
	public boolean isCorrectEntityName(String name) {
		return StringFormat.isCorrectEntityName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectFileName(java.lang.String)
	 */
	@Override
	public boolean isCorrectFileName(String name) {
		return StringFormat.isCorrectFileName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectProjectName(java.lang.String)
	 */
	@Override
	public boolean isCorrectProjectName(String name) {
		return StringFormat.isCorrectProjectName(name);
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
	 * Fires resourceSaved event.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 */
	private void fireResourceSaved(String projectName, String fileName) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceSaved(projectName, fileName);
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
