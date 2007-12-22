package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.EntityDAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * This class fully implements {@link EntityDAO} interface and in addition it
 * defines extra methods for finding entities through queries.
 * 
 * @param <T>
 *            an entity type
 * @author Miro Bezjak
 * @version 1.0.1
 * @since 27/9/2007
 */
public abstract class AbstractEntityDAO<T> implements EntityDAO<T> {

	/**
	 * A query hint key for hibernate persistence provider to set query as
	 * cacheable.
	 */
	private static final String CACHEABLE_HINT = "org.hibernate.cacheable";

	/**
	 * A class of an entity.
	 */
	private Class<T> clazz;

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            a class of an entity
	 * @throws NullPointerException
	 *             if <code>clazz</code> is <code>null</code>
	 */
	public AbstractEntityDAO(Class<T> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Entity class cant be null");
		}
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.DAOCommon#load(java.lang.Long)
	 */
	@Override
	public T load(Long id) throws DAOException {
		T entity = loadEntityImpl(id);
		if (entity == null) {
			throw new DAOException(clazz.getCanonicalName() + "[" + id + "]"
					+ " does't exit");
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.DAOCommon#save(java.lang.Object)
	 */
	@Override
	public void save(T entity) throws DAOException {
		if (entity == null) {
			throw new NullPointerException("Entity cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		try {
			em.persist(entity);
		} catch (PersistenceException e) {
			EntityManagerUtil.rollbackTransaction();
			throw new DAOException(e);
		} finally {
			EntityManagerUtil.commitTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.DAOCommon#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) throws DAOException {
		if (id == null) {
			throw new NullPointerException("Entity identifier cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		try {
			T entity = em.find(clazz, id);
			if (entity == null) {
				throw new DAOException(clazz.getCanonicalName() + "[" + id
						+ "]" + " does't exit");
			}
			em.remove(entity);
		} catch (PersistenceException e) {
			EntityManagerUtil.rollbackTransaction();
			throw new DAOException(e);
		} finally {
			EntityManagerUtil.commitTransaction();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.DAOCommon#exists(java.lang.Long)
	 */
	@Override
	public boolean exists(Long id) throws DAOException {
		T entity = loadEntityImpl(id);
		return entity != null;
	}

	/**
	 * Returns <code>true</code> if an entity was found in named query.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	public boolean existsEntity(String namedQuery, String[] paramNames,
			Object[] paramValues) throws DAOException {
		T entity = findSingleEntityImpl(namedQuery, paramNames, paramValues);
		return entity != null;
	}

	/**
	 * Returns a single entity found by specified named query. A
	 * {@link DAOException} will be thrown no entity was found by named query.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return an entity
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	public T findSingleEntity(String namedQuery, String[] paramNames,
			Object[] paramValues) throws DAOException {
		T entity = findSingleEntityImpl(namedQuery, paramNames, paramValues);
		if (entity == null) {
			throw new DAOException("No entity for such constraints");
		}
		return entity;
	}

	/**
	 * Returns a list of entities found by specified named query. A
	 * {@link DAOException} will be thrown no entity was found by named query.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	public List<T> findEntityList(String namedQuery, String[] paramNames,
			Object[] paramValues) throws DAOException {
		List<T> entities = findEntityListImpl(namedQuery, paramNames,
				paramValues);
		if (entities == null) {
			throw new DAOException("No entities for such constraints");
		}
		return entities;
	}

	/**
	 * This is an actual implementation of load entity method. If an entity with
	 * specified <code>id</code> doesn't exist then this method will return
	 * <code>null</code>.
	 * 
	 * @param id
	 *            an identifier of an entity
	 * @return an entity with specified id
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	private T loadEntityImpl(Long id) throws DAOException {
		if (id == null) {
			throw new NullPointerException("Entity identifier cant be null");
		}
		EntityManager em = EntityManagerUtil.currentEntityManager();
		EntityManagerUtil.beginTransaction();
		try {
			return em.find(clazz, id);
		} catch (PersistenceException e) {
			EntityManagerUtil.rollbackTransaction();
			throw new DAOException(e);
		} finally {
			EntityManagerUtil.commitTransaction();
		}
	}

	/**
	 * Returns a single entity found by specified named query. If no entity was
	 * found by named query then this method will return <code>null</code>.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return an entity
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	@SuppressWarnings("unchecked")
	private T findSingleEntityImpl(String namedQuery, String[] paramNames,
			Object[] paramValues) throws DAOException {
		checkParams(namedQuery, paramNames, paramValues);
		EntityManagerUtil.beginTransaction();
		T entity;
		try {
			Query query = createQuery(namedQuery, paramNames, paramValues);
			entity = (T) query.getSingleResult();
		} catch (NoResultException e) {
			EntityManagerUtil.rollbackTransaction();
			return null;
		} catch (PersistenceException e) {
			EntityManagerUtil.rollbackTransaction();
			throw new DAOException(e);
		} finally {
			EntityManagerUtil.commitTransaction();
		}
		if (entity.getClass() != clazz) {
			throw new ClassCastException("Expected " + clazz.getCanonicalName()
					+ " but found " + entity.getClass().getCanonicalName());
		}
		return entity;
	}

	/**
	 * Returns a list of entities found by specified named query. If no entity
	 * was found by named query then this method will return <code>null</code>.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return an entity list
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 * @throws ClassCastException
	 *             if query result is not of type specified through generics of
	 *             this class
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	@SuppressWarnings("unchecked")
	private List<T> findEntityListImpl(String namedQuery, String[] paramNames,
			Object[] paramValues) throws DAOException {
		checkParams(namedQuery, paramNames, paramValues);
		EntityManagerUtil.beginTransaction();
		List<T> entities;
		try {
			Query query = createQuery(namedQuery, paramNames, paramValues);
			entities = query.getResultList();
		} catch (NoResultException e) {
			EntityManagerUtil.rollbackTransaction();
			return null;
		} catch (PersistenceException e) {
			EntityManagerUtil.rollbackTransaction();
			throw new DAOException(e);
		} finally {
			EntityManagerUtil.commitTransaction();
		}
		if (!entities.isEmpty() && entities.get(0).getClass() != clazz) {
			throw new ClassCastException("Expected " + clazz.getCanonicalName()
					+ " but found "
					+ entities.get(0).getClass().getCanonicalName());
		}
		return entities;
	}

	/**
	 * Throws adequate exception if parameters are faulty.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>paramNames</code> and <code>paramValues</code>
	 *             don't have the same length
	 */
	private void checkParams(String namedQuery, String[] paramNames,
			Object[] paramValues) {
		if (namedQuery == null) {
			throw new NullPointerException("Named query cant be null");
		}
		if (paramNames.length != paramValues.length) {
			throw new IllegalArgumentException(paramNames.length
					+ " parameter names while " + paramValues.length
					+ "parameter values");
		}
	}

	/**
	 * Creates a query out of named query and specified parameters.
	 * 
	 * @param namedQuery
	 *            a name of a named query
	 * @param paramNames
	 *            an array of parameter names defined in query string
	 * @param paramValues
	 *            an array of parameter values that will replace parameter names
	 * @return created query
	 */
	private Query createQuery(String namedQuery, String[] paramNames,
			Object[] paramValues) {
		EntityManager em = EntityManagerUtil.currentEntityManager();
		Query query = em.createNamedQuery(namedQuery);
		for (int i = 0; i < paramNames.length; i++) {
			query.setParameter(paramNames[i], paramValues[i]);
		}
		/*
		 * Sets a query as cacheable. This hint is implementation dependent.
		 */
		query.setHint(CACHEABLE_HINT, Boolean.TRUE);
		return query;
	}

}
