package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerator;

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
	 * Obtains file type for the specified file.
	 * @param fileId identifier of the file
	 * @return file type
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public String getFileType(Long fileId) throws ServiceException;
	/**
	 * Obtains file name for the specified file.
	 * @param fileId identifier of the file
	 * @return file name
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public String getFileName(Long fileId) throws ServiceException;
	
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
	 * Use this method to generate VHDL for specified file. File given as
	 * argument must be of type {@linkplain File#FT_VHDLTB}}.
	 * @param file file for which VHDL must be generated
	 * @return VHDL source for specified file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public String generateTestbenchVHDL(File file) throws ServiceException;
	/**
	 * Use this method to generate VHDL for specified file. File given as
	 * argument must be of type {@linkplain File#FT_STRUCT_SCHEMA}}.
	 * @param file file for which VHDL must be generated
	 * @return VHDL source for specified file
	 * @throws ServiceException if any exception occurs (such as {@linkplain DAOException})
	 */
	public String generateShemaVHDL(File file) throws ServiceException;
}
