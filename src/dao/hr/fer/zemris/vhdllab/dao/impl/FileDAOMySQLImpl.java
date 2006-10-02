package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class FileDAOMySQLImpl implements FileDAO {
	
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
}