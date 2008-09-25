package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.EntityDAO;
import hr.fer.zemris.vhdllab.service.EntityManager;
import hr.fer.zemris.vhdllab.service.ServiceException;

/**
 * This class fully implements {@link EntityManager} interface.
 * 
 * @param <T>
 *            an entity type
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public abstract class AbstractEntityManager<T> implements EntityManager<T> {

	/**
	 * Data access object for an entity.
	 */
	protected EntityDAO<T> dao;

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public AbstractEntityManager(EntityDAO<T> dao) {
		if (dao == null) {
			throw new NullPointerException("EntityDAO cant be null");
		}
		this.dao = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.EntityManager#save(java.lang.Object)
	 */
	@Override
	public void save(T entity) throws ServiceException {
		try {
			dao.save(entity);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.EntityManager#load(java.lang.Long)
	 */
	@Override
	public T load(Integer id) throws ServiceException {
		try {
			return dao.load(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.EntityManager#exists(java.lang.Long)
	 */
	@Override
	public boolean exists(Integer id) throws ServiceException {
		try {
			return dao.exists(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.EntityManager#delete(java.lang.Long)
	 */
	@Override
	public void delete(Integer id) throws ServiceException {
		try {
			dao.delete(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
