package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>GlobalFile</code>.
 */
public interface GlobalFileDAO {

	/**
	 * Retrieves global file with specified identifier. An exception will be
	 * thrown if global file with specified identifier does not exists.
	 * 
	 * @param id
	 *            identifier of a global file.
	 * @return a global file with specified identifier.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	GlobalFile load(Long id) throws DAOException;

	/**
	 * Saves (or updates) a global file. Global file must also have constraints
	 * as described by annotations in a {@link GlobalFile} model.
	 * 
	 * @param file
	 *            a global file that will be saved (or updated).
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void save(GlobalFile file) throws DAOException;

	/**
	 * Deletes a global file. If global file does not exists then this method
	 * will throw <code>DAOException</code>.
	 * 
	 * @param file
	 *            a global file to delete
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void delete(GlobalFile file) throws DAOException;

	/**
	 * Check if a global file with specified identifier exists.
	 * 
	 * @param fileId
	 *            identifier of a global file.
	 * @return <code>true</code> if such global file exists;
	 *         <code>false</code> otherwise.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(Long fileId) throws DAOException;

	/**
	 * Check if a global file with specified name exists.
	 * 
	 * @param name
	 *            a name of global file
	 * @return <code>true</code> if such global file exists;
	 *         <code>false</code> otherwise.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(String name) throws DAOException;

	/**
	 * Returns all global files. Return value will never be <code>null</code>,
	 * although it can be an empty list.
	 * 
	 * @return all global files
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	List<GlobalFile> getAll() throws DAOException;

	/**
	 * Finds all global files whose type is specified type. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * 
	 * @param type
	 *            type of a global file
	 * @return list of global files
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	List<GlobalFile> findByType(String type) throws DAOException;

	/**
	 * Finds a global file whose name is specified <code>name</code>. Return
	 * value will never be <code>null</code>. In case that such file does not
	 * exists this method will throw {@link DAOException}.
	 * 
	 * @param name
	 *            a name of global file
	 * @return a global files
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	GlobalFile findByName(String name) throws DAOException;

}