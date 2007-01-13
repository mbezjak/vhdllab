package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.File;

/**
 * This interface defines methods for persisting model <code>File</code>.
 */
public interface FileDAO {

	/**
	 * Retrieves file with specified identifier. An exception will be thrown if file
	 * with specified identifier does not exists.
	 * @param id indentifier of a file.
	 * @return a file with specified identifier. 
	 * @throws DAOException if exceptional condition occurs.
	 */
	File load(Long id) throws DAOException;
	/**
	 * Saves (or updates) a file.
	 * @param file a file that will be saved (or updated).
	 * @throws DAOException if exceptional condition occurs.
	 */
	void save(File file) throws DAOException;
	/**
	 * Deletes a file.
	 * @param file a file to delete
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(File file) throws DAOException;
	/**
	 * Check if a file with specified identifier exists.
	 * @param fileId indentifier of a file.
	 * @return <code>true</code> if such file exists; <code>false</code> otherwise.
	 * @throws DAOException if exceptional condition occurs.
	 */
	boolean exists(Long fileId) throws DAOException;
	/**
	 * Checks to see if specified project contains a file with given name.
	 * @param projectId identifier of the project
	 * @param name name of file
	 * @return <code>true</code> if such file exists; <code>false</code> otherwise.
	 * @throws DAOException if exceptional condition occurs.
	 */
	boolean exists(Long projectId, String name) throws DAOException;
	
	/**
	 * Returns a file with specified project identifier and file name.
	 * @param projectId project identifier
	 * @param name a name of a file
	 * @return a file with specified project identifier and file name
	 * @throws DAOException if exceptional condition occurs.
	 */
	File findByName(Long projectId, String name) throws DAOException;

}
