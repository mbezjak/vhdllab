package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;

import java.util.List;

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
			HibernateUtil.closeSession();

			return file;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#save(hr.fer.zemris.vhdllab.model.File)
	 */
	public void save(File file) throws DAOException {
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
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			File f = (File) session.load(File.class, fileID);
			session.delete(f);

			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.id = :fileId")
									.setLong("fileId", fileID);
			List<File> files = (List<File>)query.list();
			
			tx.commit();
			HibernateUtil.closeSession();
			
			return files.size() != 0;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(Long projectId, String name) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.project.id = :projectId and f.fileName = :filename")
									.setLong("projectId", projectId)
									.setString("filename", name);
			List<File> files = (List<File>)query.list();

			tx.commit();
			HibernateUtil.closeSession();
			
			return files.size() != 0;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#findByName(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public File findByName(Long projectId, String name) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			Query query = session.createQuery("from File as f where f.project.id = :projectId and f.fileName = :filename")
									.setLong("projectId", projectId)
									.setString("filename", name);
			List<File> files = (List<File>)query.list();

			tx.commit();
			HibernateUtil.closeSession();

			return files.get(0);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
}