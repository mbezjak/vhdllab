package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * This class persists a <code>File</code> model using hibernate.
 * @author Miro Bezjak
 */
public class FileDAOHibernateImpl implements FileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#load(java.lang.Long)
	 */
	public File load(Long id) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			File file = (File) session.load(File.class, id);

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
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#save(hr.fer.zemris.vhdllab.model.File)
	 */
	public void save(File file) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(file);
			file.getProject().addFile(file);

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
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#delete(hr.fer.zemris.vhdllab.model.File)
	 */
	public void delete(File file) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			file.getProject().removeFile(file);
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
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileID) throws DAOException {
		if(fileID == null) {
			throw new DAOException("File identifier can not be null");
		}
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.id = :fileId")
									.setLong("fileId", fileID);
			File file = (File) query.uniqueResult();
			
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
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long, java.lang.String)
	 */
	public boolean exists(Long projectId, String name) throws DAOException {
		if(projectId == null) {
			throw new DAOException("Project identifier can not be null.");
		}
		if(name == null) {
			throw new DAOException("File name can not be null.");
		}
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.project.id = :projectId and f.fileName = :filename")
									.setLong("projectId", projectId)
									.setString("filename", name);
			File file = (File) query.uniqueResult();
			
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
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#findByName(java.lang.Long, java.lang.String)
	 */
	public File findByName(Long projectId, String name) throws DAOException {
		if(projectId == null) {
			throw new DAOException("Project identifier can not be null.");
		}
		if(name == null) {
			throw new DAOException("File name can not be null.");
		}
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.project.id = :projectId and f.fileName = :filename")
									.setLong("projectId", projectId)
									.setString("filename", name);
			File file = (File) query.uniqueResult();

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
}