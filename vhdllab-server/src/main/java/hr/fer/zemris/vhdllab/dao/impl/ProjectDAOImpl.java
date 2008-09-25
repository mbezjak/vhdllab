package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a default implementation of {@link ProjectDAO}.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class ProjectDAOImpl extends AbstractEntityDAO<Project> implements
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
	public boolean exists(Caseless userId, Caseless name) throws DAOException {
		checkParameters(userId, name);
        String query = "select p from Project as p where p.userId = :userId and p.name = :name order by p.id";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return existsEntity(query, params);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Project findByName(Caseless userId, Caseless name) throws DAOException {
		checkParameters(userId, name);
        String query = "select p from Project as p where p.userId = :userId and p.name = :name order by p.id";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return findSingleEntity(query, params);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(java.lang.String)
	 */
	@Override
	public List<Project> findByUser(Caseless userId) throws DAOException {
		checkParameter(userId);
        String query = "select p from Project as p where p.userId = :userId order by p.id";
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("userId", userId);
		return findEntityList(query, params);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(Caseless userId, Caseless name) {
		checkParameter(userId);
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

	/**
	 * Throws {@link NullPointerException} is parameter is <code>null</code>.
	 */
	private void checkParameter(Caseless userId) {
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
	}

}
