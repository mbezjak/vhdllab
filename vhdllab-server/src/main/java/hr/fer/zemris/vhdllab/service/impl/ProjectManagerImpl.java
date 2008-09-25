package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ProjectManager;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.List;

/**
 * This is a default implementation of {@link ProjectManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class ProjectManagerImpl extends AbstractEntityManager<Project>
		implements ProjectManager {

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public ProjectManagerImpl(ProjectDAO dao) {
		super(dao);
	}

	/**
	 * Returns a data access object of same type as constructor accepts it.
	 * 
	 * @return a data access object
	 */
	private ProjectDAO getDAO() {
		/*
		 * Can cast because sole constructor accepts object of this type.
		 */
		return (ProjectDAO) dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ProjectManager#exists(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Caseless userId, Caseless name) throws ServiceException {
		try {
			return getDAO().exists(userId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ProjectManager#findByName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Project findByName(Caseless userId, Caseless name)
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
	 * @see hr.fer.zemris.vhdllab.service.ProjectManager#findByUser(java.lang.String)
	 */
	@Override
	public List<Project> findByUser(Caseless userId) throws ServiceException {
		try {
			return getDAO().findByUser(userId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
