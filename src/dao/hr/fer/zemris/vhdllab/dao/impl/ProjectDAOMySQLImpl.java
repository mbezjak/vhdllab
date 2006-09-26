package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class ProjectDAOMySQLImpl implements ProjectDAO {
	
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
			
			session.save(project);
			
			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to save project!");
		}
	}

	public List<Project> findByUser(Long userId) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			Query query = session.createQuery("from Project as p where p.ownerID = :userID")
								.setLong("userID", userId.longValue());
			
			List<Project> projects = new ArrayList<Project>();
			for(Iterator it = query.iterate(); it.hasNext();) {
				projects.add((Project)it.next());
			}
			
			tx.commit();
			HibernateUtil.closeSession();
			
			return projects;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to save project!");
		}
	}
}