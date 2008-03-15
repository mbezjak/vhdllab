package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This is a default implementation of {@link UserFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class UserFileDAOImpl extends AbstractEntityDAO<UserFile>
		implements UserFileDAO {

	/**
	 * A log instance.
	 */
	private static final Logger log = Logger.getLogger(UserFileDAOImpl.class);

	/**
	 * Sole constructor.
	 */
	public UserFileDAOImpl() {
		super(UserFile.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#save(java.lang.Object)
	 */
	@Override
	public void save(UserFile file) throws DAOException {
		if (file == null) {
			throw new NullPointerException("User file cant be null");
		}
		ServerConf conf = ServerConfParser.getConfiguration();
		if (!conf.containsFileType(file.getType())) {
			if(log.isInfoEnabled()) {
				log.info("Found invalid file type: " + file.getType());
			}
			throw new DAOException(StatusCodes.DAO_INVALID_FILE_TYPE,
					"File type " + file.getType() + " is invalid!");
		}
		super.save(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#exists(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(String userId, String name) throws DAOException {
		checkParameters(userId, name);
		String namedQuery = UserFile.FIND_BY_NAME_QUERY;
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return existsEntity(namedQuery, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public UserFile findByName(String userId, String name) throws DAOException {
		checkParameters(userId, name);
		String namedQuery = UserFile.FIND_BY_NAME_QUERY;
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("name", name);
		return findSingleEntity(namedQuery, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.UserFileDAO#findByUser(java.lang.String)
	 */
	@Override
	public List<UserFile> findByUser(String userId) throws DAOException {
		checkParameter(userId);
		String namedQuery = UserFile.FIND_BY_USER_QUERY;
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("userId", userId);
		return findEntityList(namedQuery, params);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(String userId, String name) {
		checkParameter(userId);
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

	/**
	 * Throws {@link NullPointerException} is parameter is <code>null</code>.
	 */
	private void checkParameter(String userId) {
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
	}

}
