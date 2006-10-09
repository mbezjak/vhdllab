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
		return (Project)getHibernateTemplate().load(Project.class, id);
	}
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#save(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void save(Project project) throws DAOException {
		getHibernateTemplate().saveOrUpdate(project);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#delete(java.lang.Long)
	 */
	public void delete(Long projectID) throws DAOException {
		Project project = (Project)getHibernateTemplate().load(Project.class, projectID);
		getHibernateTemplate().delete(project);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByUser(Long userID) throws DAOException {
		String query = "from Project as p where p.ownerID = :userID";
		String param = "userID";
		return (List<Project>)getHibernateTemplate().findByNamedParam(query, param, userID);
	}

	public boolean exists(Long projectId) throws DAOException {
		String query = "from Project as p where p.id = :projectId";
		String param = "projectId";
		Project project = (Project) getHibernateTemplate().findByNamedParam(query, param, projectId);
		return project != null;
	}
}