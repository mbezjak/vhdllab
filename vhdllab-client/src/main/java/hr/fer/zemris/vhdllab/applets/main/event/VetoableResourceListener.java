package hr.fer.zemris.vhdllab.applets.main.event;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

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
	void beforeProjectCreation(Caseless projectName) throws ResourceVetoException;

	/**
	 * Indicates that a project has been created.
	 * 
	 * @param projectName
	 *            a name of a created project
	 */
	void projectCreated(Caseless projectName);

	/**
	 * Indicates that a project is about to be deleted. If someone vetoes this
	 * method will not rollback but will simply cancel this action.
	 * 
	 * @param projectName
	 *            a name of a project to be deleted
	 * @throws ResourceVetoException
	 *             thrown to indicate a veto for this action
	 */
	void beforeProjectDeletion(Caseless projectName) throws ResourceVetoException;

	/**
	 * Indicates that a project has been deleted.
	 * 
	 * @param projectName
	 *            a name of a deleted project
	 */
	void projectDeleted(Caseless projectName);

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
	void beforeResourceCreation(Caseless projectName, Caseless fileName, FileType type)
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
	void resourceCreated(Caseless projectName, Caseless fileName, FileType type);

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
	void beforeResourceDeletion(Caseless projectName, Caseless fileName)
			throws ResourceVetoException;

	/**
	 * Indicates that a resource has been deleted.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a deleted file
	 */
	void resourceDeleted(Caseless projectName, Caseless fileName);
	
	/**
	 * Indicates that a resource has been saved.
	 * 
	 * @param projectName
	 *            a name of a project containing a file
	 * @param fileName
	 *            a name of a deleted file
	 */
	void resourceSaved(Caseless projectName, Caseless fileName);

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
	void beforeResourceCompilation(Caseless projectName, Caseless fileName)
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
	void resourceCompiled(Caseless projectName, Caseless fileName,
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
	void beforeResourceSimulation(Caseless projectName, Caseless fileName)
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
	void resourceSimulated(Caseless projectName, Caseless fileName,
			SimulationResult result);

}
