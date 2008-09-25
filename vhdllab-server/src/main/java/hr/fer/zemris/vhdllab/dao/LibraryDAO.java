package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;

import java.util.List;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link Library} model.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public interface LibraryDAO extends EntityDAO<Library> {

	/**
	 * Returns <code>true</code> if a library with specified <code>name</code>
	 * exists.
	 * 
	 * @param name
	 *            a name of a library
	 * @return <code>true</code> if such library exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(Caseless name) throws DAOException;

	/**
	 * Finds all libraries whose <code>name</code> is specified by parameter.
	 * Return value will never be <code>null</code>. A {@link DAOException}
	 * will be thrown if such library doesn't exist.
	 * 
	 * @param name
	 *            a name of a library
	 * @return a specified library
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	Library findByName(Caseless name) throws DAOException;

	/**
	 * Finds all defined libraries. Return value will never be <code>null</code>
	 * although it can be empty list.
	 * 
	 * @return a all defined libraries
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	List<Library> getAll() throws DAOException;

}
