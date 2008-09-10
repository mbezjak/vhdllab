package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.LibraryFile;

/**
 * This interface extends {@link EntityManager} to define extra methods for
 * {@link LibraryFile} model.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface LibraryFileManager extends EntityManager<LibraryFile> {

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
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	boolean exists(Long libraryId, String name) throws ServiceException;

	/**
	 * Returns a library file with specified library identifier and library file
	 * name. Return value will never be <code>null</code>. A
	 * {@link ServiceException} will be thrown if such library file doesn't exist.
	 * 
	 * @param libraryId
	 *            a library identifier
	 * @param name
	 *            a name of a library file
	 * @return a library file with specified library identifier and library file
	 *         name
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	LibraryFile findByName(Long libraryId, String name) throws ServiceException;

}
