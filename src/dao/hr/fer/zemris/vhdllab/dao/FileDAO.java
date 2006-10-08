package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.File;

/**
 * This interface defines methods for persisting model <code>File</code>.
 */
public interface FileDAO {

	/**
	 * Returns a <code>File</code> that has file ID equal to <code>id</code>.
	 * @param id a file ID.
	 * @return a <code>File</code> that has file ID equal to <code>id</code>. 
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
	 * Deletes a file with <code>fileID</code>.
	 * @param fileID a file ID.
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(Long fileID) throws DAOException;

}
