package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class UserFileDAOHibernateImpl implements UserFileDAO {
	
	public UserFile load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			UserFile file = (UserFile) session.load(UserFile.class, id);
			
			tx.commit();
			HibernateUtil.closeSession();
			
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to load user file!");
		}
	}

	public void save(UserFile file) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			session.saveOrUpdate(file);
			
			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to save user file!");
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserFile> findByType(String type) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			
			Query query = session.createQuery("from UserFile as f where f.type = :filetype")
								.setString("filetype", type);
			
			List<UserFile> files = (List<UserFile>)query.list();
			
			tx.commit();
			HibernateUtil.closeSession();
			
			return files;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to load user files!");
		}
	}
}