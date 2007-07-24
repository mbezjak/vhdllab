package hr.fer.zemris.vhdllab.dao.impl.hibernate2;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * This class persists a <code>UserFile</code> model using hibernate
 * but uses Spring to create session and transaction.
 * @author Miro Bezjak
 */
public class UserFileDAOHibernateImplWOSession extends HibernateDaoSupport implements UserFileDAO {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#load(java.lang.Long)
	 */
	public UserFile load(Long id) throws DAOException {
		try {
			return (UserFile)getHibernateTemplate().load(UserFile.class, id);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#save(hr.fer.zemris.vhdllab.model.UserFile)
	 */
	public void save(UserFile file) throws DAOException {
		try {
			getHibernateTemplate().saveOrUpdate(file);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#delete(hr.fer.zemris.vhdllab.model.UserFile)
	 */
	public void delete(UserFile file) throws DAOException {
		try {
			getHibernateTemplate().delete(file);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<UserFile> findByUser(String userID) throws DAOException {
		if(userID == null) {
			throw new DAOException("User identifier can not be null.");
		}
		try {
			String query = "from UserFile as f where f.ownerID = :ownerID";
			String param = "ownerID";
			return (List<UserFile>)getHibernateTemplate().findByNamedParam(query, param, userID);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByName(java.lang.String, java.lang.String)
	 */
	public UserFile findByName(String userID, String name) throws DAOException {
		if(userID == null) {
			throw new DAOException("User identifier can not be null.");
		}
		if(name == null) {
			throw new DAOException("User file name can not be null.");
		}
		List<?> list;
		try {
			String query = "from UserFile as f where f.ownerID = :ownerID and f.name = :name";
			String[] params = new String[] {"ownerID", "name"};
			Object[] values = new Object[] {userID, name};
			list = (List<?>) getHibernateTemplate().findByNamedParam(query, params, values);
		} catch (Exception e) {
			throw new DAOException(e);
		}
		if(list.isEmpty()) {
			throw new DAOException("No such file!"); 
		} else {
			return (UserFile) list.get(0);
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.Long)
	 */
	public boolean exists(Long fileId) throws DAOException {
		if(fileId == null) {
			throw new DAOException("User file identifier can not be null");
		}
		try {
			String query = "from UserFile as f where f.id = :fileId";
			String param = "fileId";
			List<?> list = (List<?>) getHibernateTemplate().findByNamedParam(query, param, fileId);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.String, java.lang.String)
	 */
	public boolean exists(String ownerId, String name) throws DAOException {
		if(ownerId == null) {
			throw new DAOException("Owner identifier can not be null.");
		}
		if(name == null) {
			throw new DAOException("User file name can not be null.");
		}
		try {
			String query = "from UserFile as f where f.ownerID = :ownerId and f.name = :fileName";
			String[] params = new String[] {"ownerId", "fileName"};
			Object[] values = new Object[] {ownerId, name};
			List<?> list = (List<?>) getHibernateTemplate().findByNamedParam(query, params, values);
			return !list.isEmpty();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}