package hr.fer.zemris.vhdllab.dao.impl.hibernate2;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.model.File;

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
		return (File)getHibernateTemplate().load(File.class, id);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#save(hr.fer.zemris.vhdllab.model.File)
	 */
	public void save(File file) throws DAOException {
		getHibernateTemplate().saveOrUpdate(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		File file = (File)getHibernateTemplate().load(File.class, fileID);
		getHibernateTemplate().delete(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileId) throws DAOException {
		String query = "from File as f where f.id = :fileId";
		String param = "fileId";
		File file = (File) getHibernateTemplate().findByNamedParam(query, param, fileId);
		return file != null;
	}

	public boolean exists(Long projectId, String name) throws DAOException {
		String query = "from File as f where f.project.id = :projectId and f.fileName = :filename";
		String[] params = new String[] {"projectId", "filename"};
		Object[] values = new Object[] {projectId, name};
		File file = (File) getHibernateTemplate().findByNamedParam(query, params, values);
		return file != null;
	}

	public File findByName(Long projectId, String name) throws DAOException {
		String query = "from File as f where f.project.id = :projectId and f.fileName = :filename";
		String[] params = new String[] {"projectId", "filename"};
		Object[] values = new Object[] {projectId, name};
		File file = (File) getHibernateTemplate().findByNamedParam(query, params, values);
		return file;
	}
}