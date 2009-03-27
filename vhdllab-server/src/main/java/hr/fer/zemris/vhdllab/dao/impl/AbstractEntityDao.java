package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.EntityDao;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/**
 * Provides an implementation for {@link EntityDao}.
 * 
 * @param <T>
 *            type of an entity
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public abstract class AbstractEntityDao<T> extends JpaDaoSupport implements
        EntityDao<T> {

    /**
     * Class of an entity.
     */
    protected final Class<T> clazz;

    /**
     * Constructor.
     * 
     * @param clazz
     *            class of an entity
     * @throws NullPointerException
     *             if <code>clazz</code> is <code>null</code>
     */
    public AbstractEntityDao(Class<T> clazz) {
        Validate.notNull(clazz, "Entity class can't be null");
        this.clazz = clazz;
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDao#persist(java.lang.Object)
     */
    public void persist(T entity) {
        Validate.notNull(entity, "Entity can't be null");
        getJpaTemplate().persist(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDao#merge(java.lang.Object)
     */
    public T merge(T entity) {
        Validate.notNull(entity, "Entity can't be null");
        return getJpaTemplate().merge(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDao#load(java.lang.Integer)
     */
    @Override
    public T load(Integer id) {
        Validate.notNull(id, "Entity identifier can't be null");
        return getJpaTemplate().find(clazz, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDao#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) {
        Validate.notNull(entity, "Entity can't be null");
        getJpaTemplate().remove(entity);
    }

    /**
     * Returns a unique result for specified query string or <code>null</code>
     * if such entity doesn't exist.
     * 
     * @param queryString
     *            a query to execute
     * @param values
     *            parameter values to a query
     * @return unique result returned by query
     * @throws IllegalStateException
     *             if query returns more then 1 result
     */
    @SuppressWarnings("unchecked")
    protected final T findUniqueResult(String queryString, Object... values) {
        List<T> list = getJpaTemplate().find(queryString, values);
        if (list.size() > 1) {
            throw new IllegalStateException(
                    "Found more matches for unique result for class "
                            + clazz.getCanonicalName());
        }
        return !list.isEmpty() ? list.get(0) : null;
    }

}
