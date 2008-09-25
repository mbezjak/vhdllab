package hr.fer.zemris.vhdllab.service;


/**
 * Defines common methods for manipulating entities. An entity is any object
 * that can be persisted.
 * 
 * @param <T>
 *            type of an entity
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface EntityManager<T> {

	/**
	 * Saves or updates an existing entity. All constraints are defined in
	 * entity itself through <code>javax.persistence</code> annotations.
	 * 
	 * @param entity
	 *            an entity that will be saved or updated
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>entity</code> is <code>null</code>
	 */
	void save(T entity) throws ServiceException;

	/**
	 * Retrieves an entity with specified identifier. A {@link ServiceException}
	 * will be thrown if entity with specified identifier doesn't exist.
	 * 
	 * @param id
	 *            identifier of an entity
	 * @return an entity with specified identifier
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	T load(Integer id) throws ServiceException;

	/**
	 * Returns <code>true</code> if an entity with specified identifier exists
	 * or <code>false</code> otherwise.
	 * 
	 * @param id
	 *            identifier of an entity
	 * @return <code>true</code> if such entity exists; <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	boolean exists(Integer id) throws ServiceException;

	/**
	 * Deletes an entity. If entity doesn't exist then this method will throw
	 * <code>DAOException</code>.
	 * 
	 * @param id
	 *            an identifier of an entity to delete
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 */
	void delete(Integer id) throws ServiceException;

}
