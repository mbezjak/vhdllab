package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.OwnedEntityDao;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * Provides an implementation for {@link OwnedEntityDao}.
 * 
 * @param <T>
 *            type of an entity
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public abstract class AbstractOwnedEntityDao<T> extends AbstractEntityDao<T>
        implements OwnedEntityDao<T> {

    public AbstractOwnedEntityDao(Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByUser(String userId) {
        Validate.notNull(userId, "User identifier can't be null");
        String query = "select e from " + clazz.getSimpleName()
                + " as e where e.userId = ?1 order by e.id";
        return getJpaTemplate().find(query, userId);
    }

}
