package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class ProjectDAOHibernateImpl implements ProjectDAO {
	
	public Project load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			Project project = (Project) session.load(Project.class, id);
			
			tx.commit();
			HibernateUtil.closeSession();
			
			return project;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to load project!");
		}
	}

	public void save(Project project) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			session.saveOrUpdate(project);
			
			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to save project!");
		}
	}

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
			e.printStackTrace();
			throw new DAOException("Unable to load projects!");
		}
	}
}