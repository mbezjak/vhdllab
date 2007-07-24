package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>UserFile</code>.
 */
public interface UserFileDAO {

	/**
	 * Retrieves user file with specified identifier. An exception will be
	 * thrown if user file with specified identifier does not exists.
	 * 
	 * @param fileId
	 *            identifier of a global file.
	 * @return a user file with specified identifier.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	UserFile load(Long fileId) throws DAOException;

	/**
	 * Saves (or updates) a user file. User File must also have constraints as
	 * described by annotations in a {@link UserFile} model.
	 * 
	 * @param file
	 *            a user file that will be saved (or updated).
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void save(UserFile file) throws DAOException;

	/**
	 * Deletes a user file. If user file does not exists then this method will
	 * throw <code>DAOException</code>.
	 * 
	 * @param file
	 *            a user file to delete
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void delete(UserFile file) throws DAOException;

	/**
	 * Check if a user file with specified identifier exists.
	 * 
	 * @param fileId
	 *            identifier of a user file.
	 * @return <code>true</code> if such user file exists; <code>false</code>
	 *         otherwise.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(Long fileId) throws DAOException;

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
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(String ownerId, String name) throws DAOException;

	/**
	 * Finds all user files whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            ownerId of user file
	 * @return list of user files
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	List<UserFile> findByUser(String userId) throws DAOException;

	/**
	 * Finds a user files whose owner and name are specified by parameters.
	 * Return value will never be <code>null</code>. In case that such file
	 * does not exists this method will throw {@link DAOException}.
	 * 
	 * @param userId
	 *            ownerId of user file
	 * @param name
	 *            a name of user file
	 * @return a user file
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	UserFile findByName(String userId, String name) throws DAOException;

}
