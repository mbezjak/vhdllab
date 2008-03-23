package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.File;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link File} model.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public interface FileDAO extends EntityDAO<File> {

	/**
	 * Returns <code>true</code> if specified project contains a file with
	 * given <code>name</code> or <code>false</code> otherwise.
	 * 
	 * @param projectId
	 *            identifier of the project
	 * @param name
	 *            name of a file
	 * @return <code>true</code> if such file exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(Long projectId, String name) throws DAOException;

	/**
	 * Returns a file with specified project identifier and file name. Return
	 * value will never be <code>null</code>. A {@link DAOException} will be
	 * thrown if such file doesn't exist.
	 * 
	 * @param projectId
	 *            a project identifier
	 * @param name
	 *            a name of a file
	 * @return a file with specified project identifier and file name
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	File findByName(Long projectId, String name) throws DAOException;

}
