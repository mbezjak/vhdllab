package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>GlobalFile</code>.
 */
public interface GlobalFileDAO {

	/**
	 * Returns a <code>GlobalFile</code> that has global file ID equal to <code>id</code>.
	 * @param id a global file ID.
	 * @return a <code>GlobalFile</code> that has global file ID equal to <code>id</code>. 
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
	 * Deletes a global file with <code>fileID</code>.
	 * @param fileID a global file ID.
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(Long fileID) throws DAOException;
	/**
	 * Returns a list of <code>GlobalFile</code> that have type equal to <code>type</code>.
	 * @param type a type of a <code>GlobalFile</code>.
	 * @return a list of <code>GlobalFile</code> that have type equal to <code>type</code>.
	 * @throws DAOException if exceptional condition occurs.
	 */
	List<GlobalFile> findByType(String type) throws DAOException;

}
