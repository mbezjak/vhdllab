package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.util.StringFormat;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
     * A log instance.
     */
    private static final Logger log = Logger.getLogger(ProjectDAOImpl.class);

	/**
	 * Sole constructor.
	 */
	public ProjectDAOImpl() {
		super(Project.class);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#save(java.lang.Object)
	 */
	@Override
	public void save(Project project) throws DAOException {
	    if(project == null) {
            throw new NullPointerException("Project cant be null");
	    }
        if (!StringFormat.isCorrectProjectName(project.getName())) {
            if (log.isInfoEnabled()) {
                log.info("Found invalid project name: " + project.getName());
            }
            throw new DAOException(StatusCodes.DAO_INVALID_PROJECT_NAME,
                    "Project name " + project.getName() + " is invalid!");
        }
	    super.save(project);
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
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return existsEntity(namedQuery, params);
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
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return findSingleEntity(namedQuery, params);
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
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("userId", userId);
		return findEntityList(namedQuery, params);
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
