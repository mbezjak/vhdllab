package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.UserFileManager;

import java.util.List;

/**
 * This is a default implementation of {@link UserFileManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class UserFileManagerImpl extends AbstractEntityManager<UserFile>
		implements UserFileManager {

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public UserFileManagerImpl(UserFileDAO dao) {
		super(dao);
	}

	/**
	 * Returns a data access object of same type as constructor accepts it.
	 * 
	 * @return a data access object
	 */
	private UserFileDAO getDAO() {
		/*
		 * Can cast because sole constructor accepts object of this type.
		 */
		return (UserFileDAO) dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.UserFileManager#exists(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(String userId, String name) throws ServiceException {
		try {
			return getDAO().exists(userId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.UserFileManager#findByName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public UserFile findByName(String userId, String name)
			throws ServiceException {
		try {
			return getDAO().findByName(userId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.UserFileManager#findByUser(java.lang.String)
	 */
	@Override
	public List<UserFile> findByUser(String userId) throws ServiceException {
		try {
			return getDAO().findByUser(userId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
