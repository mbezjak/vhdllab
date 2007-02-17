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

			return project;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#save(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void save(Project project) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(project);

			tx.commit();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#delete(hr.fer.zemris.vhdllab.model.Project)
	 */
	public void delete(Project project) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.delete(project);

			tx.commit();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.ProjectDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long projectId) throws DAOException {
		if(projectId == null) {
			throw new DAOException("Project identifier can not be null.");
		}
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from Project as p where p.id = :projectId")
									.setLong("projectId", projectId);
			Project project = (Project) query.uniqueResult();
			
			tx.commit();
			
			return project != null;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
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
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from Project as p where p.ownerId = :ownerId and p.projectName = :projectName")
									.setString("ownerId", ownerId)
									.setString("projectName", projectName);
			Project project = (Project) query.uniqueResult();
			
			tx.commit();
			
			return project != null;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
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
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
	
			Query query = session.createQuery("from Project as p where p.ownerId = :userID")
									.setString("userID", userId);
	
			List<Project> projects = (List<Project>)query.list();
	
			tx.commit();
	
			return projects;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

}