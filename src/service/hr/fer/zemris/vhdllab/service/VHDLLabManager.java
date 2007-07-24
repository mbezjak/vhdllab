package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.util.List;

/**
 * This is an interface representing a VHDL Laboratory Manager. This interface
 * defines the communication between the web and the service layer.
 */
public interface VHDLLabManager {
	/**
	 * Method loads a project. An exception will be thrown if project with
	 * specified identifier does not exists.
	 * 
	 * @param projectId
	 *            identifier of project
	 * @return requested project; this will never be <code>null</code>.
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public Project loadProject(Long projectId) throws ServiceException;

	/**
	 * Check to see if specified project exists.
	 * 
	 * @param projectId
	 *            identifier of project
	 * @return <code>true</code> if project exists, <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsProject(Long projectId) throws ServiceException;

	/**
	 * Check if a project with specified <code>ownerId</code> and
	 * <code>projectName</code> exists.
	 * 
	 * @param ownerId
	 *            owner of project
	 * @param projectName
	 *            a name of a project
	 * @return <code>true</code> if such project exists; <code>false</code>
	 *         otherwise.
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	boolean existsProject(String ownerId, String projectName)
			throws ServiceException;

	/**
	 * Use this method to create a new project which has name and owner id as
	 * specified. Collection of files will be set to <code>null</code>.
	 * 
	 * @param projectName
	 *            name for the project
	 * @param ownerId
	 *            owner id for the project
	 * @return created project
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public Project createNewProject(String projectName, String ownerId)
			throws ServiceException;

	/**
	 * Finds all projects whose owner is specified user. Return value will never
	 * be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            identifier of user
	 * @return list of user's projects
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<Project> findProjectsByUser(String userId)
			throws ServiceException;

	/**
	 * Returns a project whose owner and name is specified user and project
	 * name, respectively.
	 * 
	 * @param userId
	 *            owner of project
	 * @param projectName
	 *            a name of a project
	 * @return a project whose owner and name is specified user and project
	 *         name, respectively
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public Project findProjectByName(String userId, String projectName)
			throws ServiceException;

	/**
	 * Saves projects using underlaying persistance layer.
	 * 
	 * @param p
	 *            project to save; must not be null
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveProject(Project p) throws ServiceException;

	/**
	 * Renames project with given identifier.
	 * 
	 * @param projectId
	 *            identifier of project
	 * @param newName
	 *            a new name for the project
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameProject(Long projectId, String newName)
			throws ServiceException;

	/**
	 * Use this method to delete a project.
	 * 
	 * @param projectId
	 *            identifier of a project
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteProject(Long projectId) throws ServiceException;

	/**
	 * Retrieves file with specified identifier. An exception will be thrown if
	 * file with specified identifier does not exists.
	 * 
	 * @param fileId
	 *            identifier of the file
	 * @return requested file; this will never be <code>null</code>
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public File loadFile(Long fileId) throws ServiceException;

	/**
	 * Checks to see if specified project contains a file with given name.
	 * 
	 * @param projectId
	 *            identifier of the project
	 * @param fileName
	 *            name of file
	 * @return <code>true</code> if file exists, <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsFile(Long projectId, String fileName)
			throws ServiceException;

	/**
	 * Returns a file with specified project identifier and file name.
	 * 
	 * @param projectId
	 *            project identifier
	 * @param name
	 *            a name of a file
	 * @return a file with specified project identifier and file name
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public File findFileByName(Long projectId, String name)
			throws ServiceException;

	/**
	 * Use this method to create a new file which is member of specified
	 * project, and has name and type as specified. Content will be set to
	 * <code>null</code>. Be aware that project can not contain two files
	 * with same names, so the creation in this case will result with
	 * {@linkplain ServiceException} (this is left to implementations to
	 * enforce).
	 * 
	 * @param project
	 *            project in which this file will be added
	 * @param fileName
	 *            name for the file
	 * @param fileType
	 *            type for the file
	 * @return created file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public File createNewFile(Project project, String fileName, String fileType)
			throws ServiceException;

	/**
	 * Use this method to set a new content for the file.
	 * 
	 * @param fileId
	 *            identifier of the file
	 * @param content
	 *            content for the file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveFile(Long fileId, String content) throws ServiceException;

	/**
	 * Use this method to rename a file.
	 * 
	 * @param fileId
	 *            identifier of the file
	 * @param newName
	 *            new name
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameFile(Long fileId, String newName) throws ServiceException;

	/**
	 * Use this method to delete a file.
	 * 
	 * @param fileId
	 *            identifier of a file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteFile(Long fileId) throws ServiceException;

	/**
	 * Retrieves global file with specified identifier. An exception will be
	 * thrown if global file with specified identifier does not exists.
	 * 
	 * @param fileId
	 *            identifier of the global file
	 * @return requested global file; this will never be <code>null</code>
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public GlobalFile loadGlobalFile(Long fileId) throws ServiceException;

	/**
	 * Checks to see if specified global file exists.
	 * 
	 * @param fileId
	 *            identifier of the global file
	 * @return <code>true</code> if global file exists, <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsGlobalFile(Long fileId) throws ServiceException;

	/**
	 * Check if a global file with specified name exists.
	 * 
	 * @param name
	 *            a name of global file
	 * @return <code>true</code> if such global file exists;
	 *         <code>false</code> otherwise.
	 * @throws ServiceException
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	boolean existsGlobalFile(String name) throws ServiceException;

	/**
	 * Use this method to create a new global file.
	 * 
	 * @param name
	 *            name for the global file
	 * @param type
	 *            type for the global file
	 * @return created global file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public GlobalFile createNewGlobalFile(String name, String type)
			throws ServiceException;

	/**
	 * Use this method to set a new content for the global file.
	 * 
	 * @param fileId
	 *            identifier of the global file
	 * @param content
	 *            content for the global file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveGlobalFile(Long fileId, String content)
			throws ServiceException;

	/**
	 * Use this method to rename a global file.
	 * 
	 * @param fileId
	 *            identifier of the global file
	 * @param newName
	 *            new name
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void renameGlobalFile(Long fileId, String newName)
			throws ServiceException;

	/**
	 * Use this method to delete a global file.
	 * 
	 * @param fileId
	 *            identifier of a global file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteGlobalFile(Long fileId) throws ServiceException;

	/**
	 * Finds all global files. Return value will never be <code>null</code>,
	 * although it can be an empty list.
	 * 
	 * @return all global files
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<GlobalFile> getAllGlobalFiles() throws ServiceException;

	/**
	 * Finds all global files whose type is specified type. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * 
	 * @param type
	 *            type of a global file
	 * @return list of global files
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<GlobalFile> findGlobalFilesByType(String type)
			throws ServiceException;

	/**
	 * Finds a global file whose name is specified <code>name</code>. Return
	 * value will never be <code>null</code>. In case that such file does not
	 * exists this method will throw {@link DAOException}.
	 * 
	 * @param name
	 *            a name of a global file
	 * @return a global file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public GlobalFile findGlobalFileByName(String name) throws ServiceException;

	/**
	 * Retrieves user file with specified identifier. An exception will be
	 * thrown if user file with specified identifier does not exists.
	 * 
	 * @param fileId
	 *            identifier of the user file
	 * @return requested user file; this will never be <code>null</code>
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public UserFile loadUserFile(Long fileId) throws ServiceException;

	/**
	 * Checks to see if specified user file exists.
	 * 
	 * @param fileId
	 *            identifier of the user file
	 * @return <code>true</code> if user file exists, <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public boolean existsUserFile(Long fileId) throws ServiceException;

	/**
	 * Check if a user file with specified <code>ownerId</code> and
	 * <code>name</code> exists.
	 * 
	 * @param ownerId
	 *            owner of user file
	 * @param name
	 *            a name of user file
	 * @return <code>true</code> if such user file exists; <code>false</code>
	 *         otherwise.
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	boolean existsUserFile(String ownerId, String name) throws ServiceException;

	/**
	 * Use this method to create a new user file.
	 * 
	 * @param ownerId
	 *            owner id for the user file
	 * @param name
	 *            a name of user file
	 * @param type
	 *            type for the user file
	 * @return created user file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public UserFile createNewUserFile(String ownerId, String name, String type)
			throws ServiceException;

	/**
	 * Use this method to set a new content for the user file.
	 * 
	 * @param fileId
	 *            identifier of the user file
	 * @param content
	 *            content for the user file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void saveUserFile(Long fileId, String content)
			throws ServiceException;

	/**
	 * Use this method to delete a user file.
	 * 
	 * @param fileId
	 *            identifier of a user file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public void deleteUserFile(Long fileId) throws ServiceException;

	/**
	 * Finds all user files whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            identifier of user file
	 * @return list of user files
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<UserFile> findUserFilesByUser(String userId)
			throws ServiceException;

	/**
	 * Finds a user files whose owner and name are specified by parameters.
	 * Return value will never be <code>null</code>. In case that such file
	 * does not exists this method will throw {@link DAOException}.
	 * 
	 * @param userId
	 *            user identifier of user file
	 * @param name
	 *            a name of user file
	 * @return a user file
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	public UserFile findUserFileByName(String userId, String name)
			throws ServiceException;

	/**
	 * Returns predefined file or throws <code>ServiceException</code> if
	 * predefined file with <code>fileName</code> does not exists.
	 * 
	 * @param fileName
	 *            a name of a file
	 * @param retrieveFileBody
	 *            if file content should be set
	 * @return a predefined file or throws <code>ServiceException</code> if
	 *         predefined file with <code>fileName</code> does not exists
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public File getPredefinedFile(String fileName, boolean retrieveFileBody)
			throws ServiceException;

	/**
	 * Use this method to compile specified file.
	 * 
	 * @param fileId
	 *            identifier of the file
	 * @return compilation result
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public CompilationResult compile(Long fileId) throws ServiceException;

	/**
	 * Use this method to perform a simulation. The file specified must be of
	 * simulatable type (such as {@linkplain FileTypes#FT_VHDL_TB}}).
	 * 
	 * @param fileId
	 *            identifier of the file
	 * @return simulation status
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public SimulationResult runSimulation(Long fileId) throws ServiceException;

	/**
	 * Use this method to generate VHDL for specified file. Please note that
	 * this method can return directly content of specified file (if file type
	 * is, e.g., {@linkplain FileTypes#FT_VHDL_SOURCE}. This method will
	 * dispatch the task of VHDL source generation to appropriate
	 * {@linkplain VHDLGenerator}. If no generator for file type exists, a
	 * {@linkplain ServiceException} will be thrown.
	 * 
	 * @param file
	 *            file for which VHDL must be generated
	 * @return VHDL source for specified file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public String generateVHDL(File file) throws ServiceException;

	/**
	 * Extracts circuit interface out of file content.
	 * 
	 * @param file
	 *            a file for which circuit interface must be extracted
	 * @return circuit interface for specified file
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 * @see CircuitInterface
	 */
	public CircuitInterface extractCircuitInterface(File file)
			throws ServiceException;

	/**
	 * Returns a list of files on which specified file depends on (including
	 * transitive dependencies). Return value will never be <code>null</code>,
	 * although it can be an empty list if file has no dependencies.
	 * 
	 * @param file
	 *            a file for which dependencies must be extracted
	 * @return a list of files on which specified file depends on
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<File> extractDependencies(File file) throws ServiceException;

	/**
	 * Returns a list of files on which only specified file depends on
	 * (transitive dependencies excluded). Return value will never be
	 * <code>null</code>, although it can be an empty list if file has no
	 * dependencies.
	 * 
	 * @param file
	 *            a file for which dependencies must be extracted
	 * @return a list of files on which only specified file depends on
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public List<File> extractDependenciesDisp(File file)
			throws ServiceException;

	/**
	 * Returns hierarchy of specified project. Return value will never be
	 * <code>null</code>.
	 * 
	 * @param project
	 *            a project for which to extract hierarchy
	 * @return hierarchy of specified project
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	public Hierarchy extractHierarchy(Project project) throws ServiceException;

	/**
	 * Returns a file that is not managed by DAO tier. Instead it will look for
	 * a file with <code>fileName</code> in a default location specefied by
	 * <code>defaultFiles.properties</code> configuration file.
	 * 
	 * @param fileName
	 *            a name of a file. If <code>fileName</code> contains path
	 *            elements then it must not contain any <code>../</code>
	 *            characters. Also this path will be considered relative, never
	 *            absolute.
	 * @return a default file matching <code>fileName</code>
	 * @throws ServiceException
	 *             if any exception occurs (such as {@linkplain DAOException})
	 */
	// public File getDefaultFile(String fileName) throws ServiceException;
}
