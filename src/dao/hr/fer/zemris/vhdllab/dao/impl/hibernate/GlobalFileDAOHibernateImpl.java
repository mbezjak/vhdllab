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
			HibernateUtil.closeSession();

			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to load global file!");
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
			HibernateUtil.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to save global file!");
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		try {
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();

			GlobalFile f = (GlobalFile) session.load(GlobalFile.class, fileID);
			session.delete(f);

			tx.commit();
			HibernateUtil.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to delete global file!");
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
			HibernateUtil.closeSession();

			return files;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException("Unable to load global files!");
		}
	}
}