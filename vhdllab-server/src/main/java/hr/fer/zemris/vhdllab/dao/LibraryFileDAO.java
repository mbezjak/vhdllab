package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.LibraryFile;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link LibraryFile} model.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public interface LibraryFileDAO extends EntityDAO<LibraryFile> {

	/**
	 * Returns <code>true</code> if specified library contains a library file
	 * with given <code>name</code> or <code>false</code> otherwise.
	 * 
	 * @param libraryId
	 *            identifier of the library
	 * @param name
	 *            name of a library file
	 * @return <code>true</code> if such library file exists;
	 *         <code>false</code> otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(Long libraryId, String name) throws DAOException;

	/**
	 * Returns a library file with specified library identifier and library file
	 * name. Return value will never be <code>null</code>. A
	 * {@link DAOException} will be thrown if such library file doesn't exist.
	 * 
	 * @param libraryId
	 *            a library identifier
	 * @param name
	 *            a name of a library file
	 * @return a library file with specified library identifier and library file
	 *         name
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	LibraryFile findByName(Long libraryId, String name) throws DAOException;

}
