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
		return (UserFile)getHibernateTemplate().load(UserFile.class, id);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#save(hr.fer.zemris.vhdllab.model.UserFile)
	 */
	public void save(UserFile file) throws DAOException {
		getHibernateTemplate().saveOrUpdate(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#delete(java.lang.Long)
	 */
	public void delete(Long fileID) throws DAOException {
		UserFile file = (UserFile)getHibernateTemplate().load(UserFile.class, fileID);
		getHibernateTemplate().delete(file);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<UserFile> findByUser(Long userID) throws DAOException {
		String query = "from UserFile as f where f.ownerID = :ownerID";
		String param = "ownerID";
		return (List<UserFile>)getHibernateTemplate().findByNamedParam(query, param, userID);
	}
}