package hr.fer.zemris.vhdllab.dao.impl.hibernate;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
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
			e.printStackTrace();
			throw new DAOException("Unable to load file!");
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
			e.printStackTrace();
			throw new DAOException("Unable to save file!");
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#delete(java.lang.Long)
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
			e.printStackTrace();
			throw new DAOException("Unable to delete file!");
		}
	}
}