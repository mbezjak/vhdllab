package hr.fer.zemris.vhdllab.dao.impl.hibernate2;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * This class persists a <code>Project</code> model using hibernate
 * but uses Spring to create session and transaction.
 * @author Miro Bezjak
 */
public class ProjectDAOHibernateImplWOSession extends HibernateDaoSupport implements ProjectDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#load(java.lang.Long)
	 */
	public Project load(Long id) throws DAOException {
		try {
			return (Project)getHibernateTemplate().load(Project.class, id);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#save(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void save(Project project) throws DAOException {
		try {
			getHibernateTemplate().saveOrUpdate(project);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#delete(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void delete(Project project) throws DAOException {
		try {
			getHibernateTemplate().delete(project);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long projectId) throws DAOException {
		if(projectId == null) {
			throw new DAOException("Project identifier can not be null.");
		}		try {
			String query = "from Project as p where p.id = :projectId";
			String param = "projectId";
			List<?> list = (List<?>) getHibernateTemplate().findByNamedParam(query, param, projectId);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#exists(java.lang.String, java.lang.String)
	 */
	public boolean exists(String ownerId, String projectName) throws DAOException {
		if(ownerId == null) {
			throw new DAOException("Owner identifier can not be null");
		}
		if(projectName == null) {
			throw new DAOException("Project name can not be null.");
		}
		try {
			String query = "from Project as p where p.ownerId = :ownerId and p.projectName = :projectName";
			String[] params = new String[] {"ownerId", "projectName"};
			Object[] values = new Object[] {ownerId, projectName};
			List<?> list = (List<?>) getHibernateTemplate().findByNamedParam(query, params, values);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByUser(String userId) throws DAOException {
		if(userId == null) {
			throw new DAOException("User identifier can not be null.");
		}
		try {
			String query = "from Project as p where p.ownerId = :userID";
			String param = "userID";
			return (List<Project>)getHibernateTemplate().findByNamedParam(query, param, userId);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}