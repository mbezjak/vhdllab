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
		try {
			return (GlobalFile)getHibernateTemplate().load(GlobalFile.class, id);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#save(hr.fer.zemris.vhdllab.model.GlobalFile)
	 */
	public void save(GlobalFile file) throws DAOException {
		try {
			getHibernateTemplate().saveOrUpdate(file);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#delete(hr.fer.zemris.vhdllab.model.GlobalFile)
	 */
	public void delete(GlobalFile file) throws DAOException {
		try {
			getHibernateTemplate().delete(file);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#findByType(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<GlobalFile> findByType(String type) throws DAOException {
		try {
			String query = "from GlobalFile as f where f.type = :filetype";
			String param = "filetype";
			return (List<GlobalFile>)getHibernateTemplate().findByNamedParam(query, param, type);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileId) throws DAOException {
		try {
			String query = "from GlobalFile as f where f.id = :fileId";
			String param = "fileId";
			List list = (List) getHibernateTemplate().findByNamedParam(query, param, fileId);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.String)
	 */
	public boolean exists(String name) throws DAOException {
		try {
			String query = "from GlobalFile as f where f.name = :name";
			String param = "name";
			List list = (List) getHibernateTemplate().findByNamedParam(query, param, name);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
	}
}