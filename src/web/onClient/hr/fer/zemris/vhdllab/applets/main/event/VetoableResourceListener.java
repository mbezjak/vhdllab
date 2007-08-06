package hr.fer.zemris.vhdllab.applets.main.event;

import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.util.EventListener;

/**
 * The listener interface for resource management.
 * <p>
 * Note that only before action events have a veto ability and they have no
 * rollback (they will simply cancel that action).
 * </p>
 * 
 * @author Miro Bezjak
 */
public interface VetoableResourceListener extends EventListener {

	/**
	 * Indicates that a project is about to be created. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project to be created
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeProjectCreation(String projectName) throws ResourceVetoException;

	/**
	 * Indicates that a project has been created.
	 * 
	 * @param projectName
	 *            a name of a created project
	 */
	void projectCreated(String projectName);

	/**
	 * Indicates that a project is about to be deleted. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project to be deleted
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeProjectDeletion(String projectName) throws ResourceVetoException;

	/**
	 * Indicates that a project has been deleted.
	 * 
	 * @param projectName
	 *            a name of a deleted project
	 */
	void projectDeleted(String projectName);

	/**
	 * Indicates that a resource is about to be created. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a file to be created
	 * @param type
	 *            a file type
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeResourceCreation(String projectName, String fileName, String type)
			throws ResourceVetoException;

	/**
	 * Indicates that a resource has been created.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a created file
	 * @param type
	 *            a file type
	 */
	void resourceCreated(String projectName, String fileName, String type);

	/**
	 * Indicates that a resource is about to be deleted. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a file to be deleted
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeResourceDeletion(String projectName, String fileName)
			throws ResourceVetoException;

	/**
	 * Indicates that a resource has been deleted.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a deleted file
	 */
	void resourceDeleted(String projectName, String fileName);

	/**
	 * Indicates that a resource is about to be compiled. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a file to compile
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeResourceCompilation(String projectName, String fileName)
			throws ResourceVetoException;

	/**
	 * Indicates that a resource has been compiled.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a compiled file
	 * @param result
	 *            a compilation result
	 */
	void resourceCompiled(String projectName, String fileName,
			CompilationResult result);

	/**
	 * Indicates that a resource is about to be simulated. If someone vetoes
	 * this method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a file to simulate
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeResourceSimulation(String projectName, String fileName)
			throws ResourceVetoException;

	/**
	 * Indicates that a resource has been simulated.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a simulated file
	 * @param result
	 *            a simulation result
	 */
	void resourceSimulated(String projectName, String fileName,
			SimulationResult result);

}
