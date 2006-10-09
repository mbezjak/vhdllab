package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * This class persists a <code>Project</code> model using hibernate.
 * @author Miro Bezjak
 */
public class ProjectDAOHibernateImpl implements ProjectDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#load(java.lang.Long)
	 */
	public Project load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Project project = (Project) session.load(Project.class, id);

			tx.commit();
			HibernateUtil.closeSession();

			return project;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#save(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void save(Project project) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(project);

			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#delete(java.lang.Long)
	 */
	public void delete(Long projectID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Project p = (Project) session.load(Project.class, projectID);
			session.delete(p);

			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Project> findByUser(Long userId) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from Project as p where p.ownerID = :userID")
									.setLong("userID", userId.longValue());

			List<Project> projects = (List<Project>)query.list();

			tx.commit();
			HibernateUtil.closeSession();

			return projects;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#exists(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(Long projectId) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from Project as p where p.id = :projectId")
									.setLong("projectId", projectId);
			List<Project> projects = (List<Project>)query.list();

			tx.commit();
			HibernateUtil.closeSession();
			
			return projects.size() != 0;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

}