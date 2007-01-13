package hr.fer.zemris.vhdllab.dao.impl.hibernate2;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * This class persists a <code>File</code> model using hibernate
 * but uses Spring to create session and transaction.
 * @author Miro Bezjak
 */
public class FileDAOHibernateImplWOSession extends HibernateDaoSupport implements FileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#load(java.lang.Long)
	 */
	public File load(Long id) throws DAOException {
		try {
			return (File)getHibernateTemplate().load(File.class, id);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#save(hr.fer.zemris.vhdllab.model.File)
	 */
	public void save(File file) throws DAOException {
		try {
			getHibernateTemplate().saveOrUpdate(file);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#delete(hr.fer.zemris.vhdllab.model.File)
	 */
	public void delete(File file) throws DAOException {
		try {
			getHibernateTemplate().delete(file);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileId) throws DAOException {
		try {
			String query = "from File as f where f.id = :fileId";
			String param = "fileId";
			List list = (List) getHibernateTemplate().findByNamedParam(query, param, fileId);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	public boolean exists(Long projectId, String name) throws DAOException {
		try {
			String query = "from File as f where f.project.id = :projectId and f.fileName = :filename";
			String[] params = new String[] {"projectId", "filename"};
			Object[] values = new Object[] {projectId, name};
			List list = (List) getHibernateTemplate().findByNamedParam(query, params, values);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	public File findByName(Long projectId, String name) throws DAOException {
		try {
			String query = "from File as f where f.project.id = :projectId and f.fileName = :filename";
			String[] params = new String[] {"projectId", "filename"};
			Object[] values = new Object[] {projectId, name};
			File file = (File) getHibernateTemplate().findByNamedParam(query, params, values);
			return file;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
}