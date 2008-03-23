package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.List;

/**
 * This is a default implementation of {@link ProjectDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 27/9/2007
 */
public class ProjectDAOImpl extends AbstractEntityDAO<Project> implements
		ProjectDAO {

	/**
	 * Sole constructor.
	 */
	public ProjectDAOImpl() {
		super(Project.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#exists(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(String userId, String name) throws DAOException {
		checkParameters(userId, name);
		String namedQuery = Project.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "userId", "name" };
		Object[] paramValues = new Object[] { userId, name };
		return existsEntity(namedQuery, paramNames, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Project findByName(String userId, String name) throws DAOException {
		checkParameters(userId, name);
		String namedQuery = Project.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "userId", "name" };
		Object[] paramValues = new Object[] { userId, name };
		return findSingleEntity(namedQuery, paramNames, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(java.lang.String)
	 */
	@Override
	public List<Project> findByUser(String userId) throws DAOException {
		checkParameter(userId);
		String namedQuery = Project.FIND_BY_USER_QUERY;
		String[] paramNames = new String[] { "userId" };
		Object[] paramValues = new Object[] { userId };
		return findEntityList(namedQuery, paramNames, paramValues);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(String userId, String name) {
		checkParameter(userId);
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

	/**
	 * Throws {@link NullPointerException} is parameter is <code>null</code>.
	 */
	private void checkParameter(String userId) {
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
	}

}
