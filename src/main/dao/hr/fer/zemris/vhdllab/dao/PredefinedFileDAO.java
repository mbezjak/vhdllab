package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.File;

import java.util.List;

/**
 * Defines methods for loading predefined files. A predefined file is a regular
 * file however they don't belong to a project (nor to a user). Instead any user
 * can use this files in any of his project. These files are written in advance
 * and given to a user as some sort of library. For example: a user developing
 * his own instance of XOR circuit will need basic (predefined) circuits (NOT,
 * AND, OR).
 * 
 * Note that predefined files can't be modified or deleted.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 21/10/2007
 */
public interface PredefinedFileDAO {

	/**
	 * Retrieves a predefined file with specified name. A {@link DAOException}
	 * will be thrown if file with specified name doesn't exist.
	 * 
	 * @param name
	 *            a name of a predefined file
	 * @return an entity with specified name
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	File load(String name) throws DAOException;

	/**
	 * Returns <code>true</code> if a predefined file with specified name
	 * exists or <code>false</code> otherwise.
	 * 
	 * @param name
	 *            name of a predefined file
	 * @return <code>true</code> if such file exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	boolean exists(String name) throws DAOException;

	/**
	 * Returns all predefined files. Return value will never be
	 * <code>null</code> although it can be empty list.
	 * 
	 * @return all predefined files
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	List<File> getAll() throws DAOException;

}
