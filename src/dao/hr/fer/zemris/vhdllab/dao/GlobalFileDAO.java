package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>GlobalFile</code>.
 */
public interface GlobalFileDAO {

	/**
	 * Retrieves global file with specified identifier. An exception will be thrown if
	 * global file with specified identifier does not exists.
	 * @param id indentifier of a global file.
	 * @return a global file with specified identifier. 
	 * @throws DAOException if exceptional condition occurs.
	 */
	GlobalFile load(Long id) throws DAOException;
	/**
	 * Saves (or updates) a global file.
	 * @param file a global file that will be saved (or updated).
	 * @throws DAOException if exceptional condition occurs.
	 */
	void save(GlobalFile file) throws DAOException;
	/**
	 * Deletes a global file.
	 * @param file a global file to delete
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(GlobalFile file) throws DAOException;
	/**
	 * Finds all global files whose type is specified type. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * @param type type of a global file
	 * @return list of global files
	 * @throws DAOException if exceptional condition occurs.
	 */
	List<GlobalFile> findByType(String type) throws DAOException;
	/**
	 * Check if a global file with specified identifier exists.
	 * @param fileId indentifier of a global file.
	 * @return <code>true</code> if such global file exists; <code>false</code> otherwise.
	 * @throws DAOException if exceptional condition occurs.
	 */
	boolean exists(Long fileId) throws DAOException;
}
