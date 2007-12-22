package hr.fer.zemris.vhdllab.dao;

/**
 * Defines 4 common methods for manipulating entities. An entity is any object
 * that can be persisted in DAO layer.
 * 
 * @param <T>
 *            type of an entity
 * @author Miro Bezjak
 * @version 1.0
 * @since 27/9/2007
 */
public interface EntityDAO<T> {

	/**
	 * Retrieves an entity with specified identifier. A {@link DAOException}
	 * will be thrown if entity with specified identifier doesn't exist.
	 * 
	 * @param id
	 *            identifier of an entity
	 * @return an entity with specified identifier
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	T load(Long id) throws DAOException;

	/**
	 * Saves (or updates) an entity. All constraints are defined in entity
	 * itself through <code>javax.persistence</code> annotations.
	 * 
	 * @param entity
	 *            an entity that will be saved (or updated)
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>entity</code> is <code>null</code>
	 */
	void save(T entity) throws DAOException;

	/**
	 * Deletes an entity. If entity doesn't exist then this method will throw
	 * <code>DAOException</code>.
	 * 
	 * @param id
	 *            an identifier of an entity to delete
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	void delete(Long id) throws DAOException;

	/**
	 * Returns <code>true</code> if an entity with specified identifier exists
	 * or <code>false</code> otherwise.
	 * 
	 * @param id
	 *            identifier of an entity
	 * @return <code>true</code> if such entity exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	boolean exists(Long id) throws DAOException;

}
