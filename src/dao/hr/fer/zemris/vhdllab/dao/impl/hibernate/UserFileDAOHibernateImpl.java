package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * This class persists a <code>UserFile</code> model using hibernate.
 * @author Miro Bezjak
 */
public class UserFileDAOHibernateImpl implements UserFileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#load(java.lang.Long)
	 */
	public UserFile load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			UserFile file = (UserFile) session.load(UserFile.class, id);

			tx.commit();

			return file;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#save(hr.fer.zemris.vhdllab.model.UserFile)
	 */
	public void save(UserFile file) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(file);

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
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#delete(hr.fer.zemris.vhdllab.model.UserFile)
	 */
	public void delete(UserFile file) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.delete(file);

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
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<UserFile> findByUser(String userID) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from UserFile as f where f.ownerID = :ownerID")
									.setString("ownerID", userID);

			List<UserFile> files = (List<UserFile>)query.list();

			tx.commit();

			return files;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from UserFile as f where f.id = :fileId")
									.setLong("fileId", fileID);
			UserFile file = (UserFile) query.uniqueResult();
			
			tx.commit();

			return file != null;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.String, java.lang.String)
	 */
	public boolean exists(String ownerId, String name) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from UserFile as f where f.ownerID = :ownerId and f.name = :fileName")
									.setString("ownerId", ownerId)
									.setString("fileName", name);
			UserFile file = (UserFile) query.uniqueResult();
			
			tx.commit();

			return file != null;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Throwable ignored) {}
		}
	}
}