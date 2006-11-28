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
			HibernateUtil.closeSession();

			return file;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
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
			HibernateUtil.closeSession();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			UserFile f = (UserFile) session.load(UserFile.class, fileID);
			session.delete(f);

			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
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
			HibernateUtil.closeSession();

			return files;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from UserFile as f where f.id = :fileId")
									.setLong("fileId", fileID);
			List<UserFile> files = (List<UserFile>)query.list();

			tx.commit();
			HibernateUtil.closeSession();

			return files.size() != 0;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
}