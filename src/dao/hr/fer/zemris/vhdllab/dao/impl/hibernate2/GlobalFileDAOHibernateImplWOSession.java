package hr.fer.zemris.vhdllab.dao.impl.hibernate2;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.model.GlobalFile;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * This class persists a <code>GlobalFile</code> model using hibernate
 * but uses Spring to create session and transaction.
 * @author Miro Bezjak
 */
public class GlobalFileDAOHibernateImplWOSession extends HibernateDaoSupport implements GlobalFileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#load(java.lang.Long)
	 */
	public GlobalFile load(Long id) throws DAOException {
		return (GlobalFile)getHibernateTemplate().load(GlobalFile.class, id);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#save(hr.fer.zemris.vhdllab.model.GlobalFile)
	 */
	public void save(GlobalFile file) throws DAOException {
		getHibernateTemplate().saveOrUpdate(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		GlobalFile file = (GlobalFile)getHibernateTemplate().load(GlobalFile.class, fileID);
		getHibernateTemplate().delete(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#findByType(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<GlobalFile> findByType(String type) throws DAOException {
		String query = "from GlobalFile as f where f.type = :filetype";
		String param = "filetype";
		return (List<GlobalFile>)getHibernateTemplate().findByNamedParam(query, param, type);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileId) throws DAOException {
		String query = "from GlobalFile as f where f.id = :fileId";
		String param = "fileId";
		GlobalFile file = (GlobalFile) getHibernateTemplate().findByNamedParam(query, param, fileId);
		return file != null;
	}
}