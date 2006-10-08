package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>UserFile</code>.
 */
public interface UserFileDAO {
	
	/**
	 * Returns a <code>UserFile</code> that has user file ID equal to <code>id</code>.
	 * @param id a user file ID.
	 * @return a <code>UserFile</code> that has user file ID equal to <code>id</code>. 
	 * @throws DAOException if exceptional condition occurs.
	 */
	UserFile load(Long ownerID) throws DAOException;
	/**
	 * Saves (or updates) a user file.
	 * @param file a user file that will be saved (or updated).
	 * @throws DAOException if exceptional condition occurs.
	 */
	void save(UserFile file) throws DAOException;
	/**
	 * Deletes a user file with <code>fileID</code>.
	 * @param fileID a user file ID.
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(Long fileID) throws DAOException;
	/**
	 * Returns a list of <code>UserFile</code> that have ownerID equal to <code>userID</code>.
	 * @param userID an ownerID of a <code>UserFile</code>.
	 * @return a list of <code>UserFile</code> that have ownerID equal to <code>userID</code>.
	 * @throws DAOException if exceptional condition occurs.
	 */
	List<UserFile> findByUser(Long userID) throws DAOException;
	
}
