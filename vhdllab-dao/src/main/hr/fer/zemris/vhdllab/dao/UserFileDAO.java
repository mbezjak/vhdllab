package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.List;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link UserFile} model.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public interface UserFileDAO extends EntityDAO<UserFile> {
	
	/**
	 * Returns <code>true</code> if a user file with specified
	 * <code>ownerId</code> and <code>name</code> exists or
	 * <code>false</code> otherwise.
	 * 
	 * @param userId
	 *            owner of a user file
	 * @param name
	 *            a name of a user file
	 * @return <code>true</code> if such user file exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(String userId, String name) throws DAOException;

	/**
	 * Finds all user files whose owner and name are specified by parameters.
	 * Return value will never be <code>null</code>. A {@link DAOException}
	 * will be thrown if such user file doesn't exist.
	 * 
	 * @param userId
	 *            owner of a user file
	 * @param name
	 *            a name of a user file
	 * @return a user file
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	UserFile findByName(String userId, String name) throws DAOException;

	/**
	 * Finds all user files whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            owner of a user file
	 * @return list of user files
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	List<UserFile> findByUser(String userId) throws DAOException;

}
