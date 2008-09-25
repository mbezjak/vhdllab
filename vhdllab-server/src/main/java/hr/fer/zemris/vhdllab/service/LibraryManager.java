package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;

import java.util.List;

/**
 * This interface extends {@link EntityManager} to define extra methods for
 * {@link Library} model.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface LibraryManager extends EntityManager<Library> {

	/**
	 * Returns <code>true</code> if a library with specified <code>name</code>
	 * exists.
	 * 
	 * @param name
	 *            a name of a library
	 * @return <code>true</code> if such library exists; <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	boolean exists(Caseless name) throws ServiceException;

	/**
	 * Finds all libraries whose <code>name</code> is specified by parameter.
	 * Return value will never be <code>null</code>. A
	 * {@link ServiceException} will be thrown if such library doesn't exist.
	 * 
	 * @param name
	 *            a name of a library
	 * @return a specified library
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	Library findByName(Caseless name) throws ServiceException;

	/**
	 * Returns a library with predefined files.
	 * 
	 * @return a library with predefined files
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	Library getPredefinedLibrary() throws ServiceException;

	/**
	 * Finds all defined libraries. Return value will never be <code>null</code>
	 * although it can be empty list.
	 * 
	 * @return a all defined libraries
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	List<Library> getAll() throws ServiceException;

}
