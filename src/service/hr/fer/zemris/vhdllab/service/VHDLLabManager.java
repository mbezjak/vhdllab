package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;

/**
 * This is an interface representing a VHDL Laboratory Manager. 
 * This interface defines the communication between the web 
 * and the service layer.
 */
public interface VHDLLabManager {
	/**
	 * Method loads a project. An exception will be thrown if project
	 * with specified identifier does not exists.
	 * @param projectId identifier of project
	 * @return requested project; this will never be <code>null</code>.
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public Project loadProject(Long projectId) throws ServiceException;
	/**
	 * Check to see if specified project exists.
	 * @param projectId identifier of project
	 * @return <code>true</code> if project exists, <code>false</code> otherwise
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsProject(Long projectId) throws ServiceException;
	/**
	 * Use this method to create a new project which has name and owner id as
	 * specified. Collection of files will be set to <code>null</code>. 
	 * @param projectName name for the project
	 * @param ownerId owner id for the project
	 * @return created project
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public Project createNewProject(String projectName, Long ownerId) throws ServiceException;
	/**
	 * Finds all projects whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * @param userId identifier of user
	 * @return list of user's projects
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<Project> findProjectsByUser(Long userId) throws ServiceException;
	/**
	 * Saves projects using underlaying persistance layer.
	 * @param p project to save; must not be null
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveProject(Project p) throws ServiceException;
	/**
	 * Renames project with given identifier.
	 * @param projectId identifier of project
	 * @param newName a new name for the project
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameProject(Long projectId, String newName) throws ServiceException;
	/**
	 * Use this method to delete a project.
	 * @param projectId identifier of a project
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteProject(Long projectId) throws ServiceException;
	
	/**
	 * Retrieves file with specified identifier. An exception will be thrown if file
	 * with specified identifier does not exists.
	 * @param fileId identifier of the file
	 * @return requested file; this will never be <code>null</code>
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public File loadFile(Long fileId) throws ServiceException;
	/**
	 * Checks to see if specified project contains a file with given name.
	 * @param projectId identifier of the project
	 * @param fileName name of file
	 * @return <code>true</code> if file exists, <code>false</code> otherwise
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsFile(Long projectId, String fileName) throws ServiceException;
	/**
	 * Use this method to create a new file which is member of specified
	 * project, and has name and type as specified. Content will be set
	 * to <code>null</code>. Be aware that project can not contain two
	 * files with same names, so the creation in this case will result
	 * with {@linkplain ServiceException} (this is left to implementations
	 * to enforce).
	 * @param project project in which this file will be added 
	 * @param fileName name for the file
	 * @param fileType type for the file
	 * @return created file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public File createNewFile(Project project, String fileName, String fileType) throws ServiceException;
	/**
	 * Use this method to set a new content for the file.
	 * @param fileId identifier of the file
	 * @param content content for the file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveFile(Long fileId, String content) throws ServiceException;
	/**
	 * Use this method to rename a file.
	 * @param fileId identifier of the file
	 * @param newName new name
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameFile(Long fileId, String newName) throws ServiceException;
	/**
	 * Use this method to delete a file.
	 * @param fileId identifier of a file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteFile(Long fileId) throws ServiceException;

	/**
	 * Retrieves global file with specified identifier. An exception will be thrown if global file
	 * with specified identifier does not exists.
	 * @param fileId identifier of the global file
	 * @return requested global file; this will never be <code>null</code>
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public GlobalFile loadGlobalFile(Long fileId) throws ServiceException;
	/**
	 * Checks to see if specified global file exists.
	 * @param fileId identifier of the global file
	 * @return <code>true</code> if global file exists, <code>false</code> otherwise
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsGlobalFile(Long fileId) throws ServiceException;
	/**
	 * Use this method to create a new global file.
	 * @param name name for the global file
	 * @param type type for the global file
	 * @return created global file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public GlobalFile createNewGlobalFile(String name, String type) throws ServiceException;
	/**
	 * Use this method to set a new content for the global file.
	 * @param fileId identifier of the global file
	 * @param content content for the global file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveGlobalFile(Long fileId, String content) throws ServiceException;
	/**
	 * Use this method to rename a global file.
	 * @param fileId identifier of the global file
	 * @param newName new name
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameGlobalFile(Long fileId, String newName) throws ServiceException;
	/**
	 * Use this method to delete a global file.
	 * @param fileId identifier of a global file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteGlobalFile(Long fileId) throws ServiceException;
	/**
	 * Finds all global files whose type is specified type. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * @param type type of a global file
	 * @return list of global files
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<GlobalFile> findGlobalFilesByType(String type) throws ServiceException;
	
	/**
	 * Retrieves user file with specified identifier. An exception will be thrown if user file
	 * with specified identifier does not exists.
	 * @param fileId identifier of the user file
	 * @return requested user file; this will never be <code>null</code>
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public UserFile loadUserFile(Long fileId) throws ServiceException;
	/**
	 * Checks to see if specified user file exists.
	 * @param fileId identifier of the user file
	 * @return <code>true</code> if user file exists, <code>false</code> otherwise
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsUserFile(Long fileId) throws ServiceException;
	/**
	 * Use this method to create a new user file.
	 * @param ownerId owner id for the user file
	 * @param type type for the user file
	 * @return created user file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public UserFile createNewUserFile(Long ownerId, String type) throws ServiceException;
	/**
	 * Use this method to set a new content for the user file.
	 * @param fileId identifier of the user file
	 * @param content content for the user file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveUserFile(Long fileId, String content) throws ServiceException;
	/**
	 * Use this method to delete a user file.
	 * @param fileId identifier of a user file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteUserFile(Long fileId) throws ServiceException;
	/**
	 * Finds all user files whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * @param userId identifier of user file
	 * @return list of user files
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<UserFile> findUserFilesByUser(Long userId) throws ServiceException;

	/**
	 * Use this method to compile specified file.
	 * @param fileId identifier of the file
	 * @return compilation result
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public CompilationResult compile(Long fileId) throws ServiceException;
	/**
	 * Use this method to perform a simulation. The file specified
	 * must be of simulatable type (such as {@linkplain File#FT_VHDLTB}}).
	 * @param fileId identifier of the file
	 * @return simulation status
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public SimulationResult runSimulation(Long fileId) throws ServiceException;
	
	/**
	 * Use this method to generate VHDL for specified file. Please note that
	 * this method can return directly content of specified file (if file type
	 * is, e.g., {@linkplain File#FT_VHDLSOURCE}. This method will dispatch
	 * the task of VHDL source generation to appropriate {@linkplain VHDLGenerator}.
	 * If no generator for file type exists, a {@linkplain ServiceException} will
	 * be thrown.
	 * @param file file for which VHDL must be generated
	 * @return VHDL source for specified file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public String generateVHDL(File file) throws ServiceException;
	
	/**
	 * Extracts CircuitInterface base on file content, however which extractor will be used
	 * is based on file type.
	 * @param file file for which CircuitInterface must be extracted
	 * @return CircuitInterface CircuitInterface for specified file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 * @see CircuitInterface
	 */
	public CircuitInterface extractCircuitInterface(File file) throws ServiceException;
}
