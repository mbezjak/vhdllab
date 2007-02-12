package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * This class persists a <code>GlobalFile</code> model using hibernate.
 * @author Miro Bezjak
 */
public class GlobalFileDAOHibernateImpl implements GlobalFileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#load(java.lang.Long)
	 */
	public GlobalFile load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			GlobalFile file = (GlobalFile) session.load(GlobalFile.class, id);

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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#save(hr.fer.zemris.vhdllab.model.GlobalFile)
	 */
	public void save(GlobalFile file) throws DAOException {
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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#delete(hr.fer.zemris.vhdllab.model.GlobalFile)
	 */
	public void delete(GlobalFile file) throws DAOException {
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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#findByType(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<GlobalFile> findByType(String type) throws DAOException {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from GlobalFile as f where f.type = :filetype")
									.setString("filetype", type);

			List<GlobalFile> files = (List<GlobalFile>)query.list();

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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from GlobalFile as f where f.id = :fileId")
									.setLong("fileId", fileID);
			GlobalFile file = (GlobalFile) query.uniqueResult();
			
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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.String)
	 */
	public boolean exists(String name) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from GlobalFile as f where f.name = :name")
									.setString("name", name);
			GlobalFile file = (GlobalFile) query.uniqueResult();
			
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