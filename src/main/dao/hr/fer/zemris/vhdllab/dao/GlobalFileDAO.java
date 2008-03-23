package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.GlobalFile;

import java.util.List;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link GlobalFile} model.
 * 
 * @author Miro Bezjak
 */
public interface GlobalFileDAO extends EntityDAO<GlobalFile> {

	/**
	 * Returns <code>true</code> if a global file with specified
	 * <code>name</code> exists or <code>false</code> otherwise.
	 * 
	 * @param name
	 *            a name of global file
	 * @return <code>true</code> if global file with specified
	 *         <code>name</code> exits; <code>false</code> otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(String name) throws DAOException;

	/**
	 * Finds a global file whose name is specified <code>name</code>. Return
	 * value will never be <code>null</code>. A {@link DAOException} will be
	 * thrown if such global file doesn't exist.
	 * 
	 * @param name
	 *            a name of a global file
	 * @return a global files
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	GlobalFile findByName(String name) throws DAOException;

	/**
	 * Returns all global files. Return value will never be <code>null</code>,
	 * although it can be an empty list.
	 * 
	 * @return all global files
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	List<GlobalFile> getAll() throws DAOException;

}