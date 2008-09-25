package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.util.StringFormat;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

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
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createNewResource(Caseless projectName, Caseless fileName,
			FileType type, String data) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		if (type == null) {
			throw new NullPointerException("File type cant be null");
		}
		if (data == null) {
		    throw new NullPointerException("File data cant be null");
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
			// FIXME should this return false?
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#createNewProject(java.lang.String)
	 */
	@Override
	public boolean createNewProject(Caseless projectName)
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
            // FIXME should this return false?
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
	public void deleteFile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if(isPredefined(projectName, fileName)) {
		    return;
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
	public void deleteProject(Caseless projectName) throws UniformAppletException {
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
	public boolean existsFile(Caseless projectName, Caseless fileName)
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
	public boolean existsProject(Caseless projectName)
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
	public List<Caseless> getAllProjects() throws UniformAppletException {
		return communicator.getAllProjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#getFilesFor(java.lang.String)
	 */
	@Override
	public List<Caseless> getFilesFor(Caseless projectName)
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
	public List<Caseless> getAllCircuits(Caseless projectName)
			throws UniformAppletException {
		List<Caseless> fileNames = getFilesFor(projectName);
		List<Caseless> circuits = new ArrayList<Caseless>();
		for (Caseless name : fileNames) {
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
	public List<Caseless> getAllTestbenches(Caseless projectName)
			throws UniformAppletException {
		List<Caseless> fileNames = getFilesFor(projectName);
		List<Caseless> testbenches = new ArrayList<Caseless>();
		for (Caseless name : fileNames) {
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
	public CircuitInterface getCircuitInterfaceFor(Caseless projectName,
	        Caseless fileName) throws UniformAppletException {
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
	public VHDLGenerationResult generateVHDL(Caseless projectName, Caseless fileName)
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
	public String getFileContent(Caseless projectName, Caseless fileName)
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
	public String getPredefinedFileContent(Caseless fileName)
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
	public void saveFile(Caseless projectName, Caseless fileName, String content)
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
	public FileType getFileType(Caseless projectName, Caseless fileName) {
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
	public Hierarchy extractHierarchy(Caseless projectName)
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
	public CompilationResult compile(Caseless projectName, Caseless fileName)
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
	public SimulationResult simulate(Caseless projectName, Caseless fileName)
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
	public boolean isCircuit(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		FileType type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileType.isCircuit(type);
	}

	/*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isPredefined
     * (java.lang.String, java.lang.String)
     */
	@Override
	public boolean isPredefined(Caseless projectName, Caseless fileName) {
	    if (projectName == null || fileName == null) {
	        return false;
	    }
	    FileType type = getFileType(projectName, fileName);
	    if (type == null) {
	        return false;
	    }
	    return type.equals(FileType.PREDEFINED);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isTestbench(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isTestbench(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		FileType type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		if (type.equals(FileType.TESTBENCH)) {
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
	public boolean isSimulation(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		FileType type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return type.equals(FileType.SIMULATION);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCompilable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCompilable(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		FileType type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return FileType.isCircuit(type) && !type.equals(FileType.PREDEFINED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isSimulatable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulatable(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		FileType type = getFileType(projectName, fileName);
		if (type == null) {
			return false;
		}
		return type.equals(FileType.TESTBENCH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#canGenerateVHDLCode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean canGenerateVHDLCode(Caseless projectName, Caseless fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		return (isCircuit(projectName, fileName)
				|| isTestbench(projectName, fileName))
				&& !isPredefined(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectEntityName(java.lang.String)
	 */
	@Override
	public boolean isCorrectEntityName(Caseless name) {
		return StringFormat.isCorrectEntityName(name.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectFileName(java.lang.String)
	 */
	@Override
	public boolean isCorrectFileName(Caseless name) {
		return StringFormat.isCorrectFileName(name.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#isCorrectProjectName(java.lang.String)
	 */
	@Override
	public boolean isCorrectProjectName(Caseless name) {
		return StringFormat.isCorrectProjectName(name.toString());
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
	private void fireBeforeProjectCreation(Caseless projectName)
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
	private void fireProjectCreated(Caseless projectName)
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
	private void fireBeforeProjectDeletion(Caseless projectName)
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
	private void fireProjectDeleted(Caseless projectName) {
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
	private void fireBeforeResourceCreation(Caseless projectName,
	        Caseless fileName, FileType type) throws ResourceVetoException {
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
	private void fireResourceCreated(Caseless projectName, Caseless fileName,
			FileType type) throws ResourceVetoException {
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
	private void fireBeforeResourceDeletion(Caseless projectName, Caseless fileName)
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
	private void fireResourceDeleted(Caseless projectName, Caseless fileName) {
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
	private void fireBeforeResourceCompilation(Caseless projectName,
	        Caseless fileName) throws ResourceVetoException {
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
	private void fireResourceSaved(Caseless projectName, Caseless fileName) {
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
	private void fireResourceCompiled(Caseless projectName, Caseless fileName,
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
	private void fireBeforeResourceSimulation(Caseless projectName,
	        Caseless fileName) throws ResourceVetoException {
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
	private void fireResourceSimulated(Caseless projectName, Caseless fileName,
			SimulationResult result) {
		for (VetoableResourceListener l : getVetoableResourceListeners()) {
			l.resourceSimulated(projectName, fileName, result);
		}
	}

}
