package hr.fer.zemris.vhdllab.dao;

import org.springframework.transaction.annotation.Transactional;

/**
 * Defines common methods for manipulating entities. An entity is any object
 * that can be persisted by its data access object (DAO).
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @param <T>
 *            type of an entity
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface EntityDao<T> {

    /**
     * Saves or updates an existing entity. All constraints are defined in
     * entity itself through <code>javax.persistence</code> annotations.
     * 
     * @param entity
     *            an entity that will be saved or updated
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    void save(T entity);

    /**
     * Retrieves an entity with specified identifier. <code>null</code> value
     * will be returned if such entity doesn't exist.
     * 
     * @param id
     *            identifier of an entity
     * @return an entity with specified identifier or <code>null</code> if such
     *         entity doesn't exist
     * @throws NullPointerException
     *             if <code>id</code> is <code>null</code>
     */
    T load(Integer id);

    /**
     * Deletes an entity.
     * 
     * @param entity
     *            an entity to delete
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    void delete(T entity);

}
