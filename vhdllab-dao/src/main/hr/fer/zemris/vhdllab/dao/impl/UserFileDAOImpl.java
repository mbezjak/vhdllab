package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a default implementation of {@link UserFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class UserFileDAOImpl extends AbstractDatabaseEntityDAO<UserFile>
		implements UserFileDAO {
	
	/**
	 * Sole constructor.
	 */
	public UserFileDAOImpl() {
		super(UserFile.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractDatabaseEntityDAO#create(java.lang.Object)
	 */
	@Override
	public void create(UserFile entity) throws DAOException {
		if(entity.getName() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file name is null");
		}
		if(entity.getType() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file type is null");
		}
		if(entity.getContent() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file content is null");
		}
		if(entity.getUserId() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User identifier is null");
		}
		if(entity.getCreated() != null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "Created date is set");
		}
		if(entity.getName().length() > DAOUtil.columnLengthFor(UserFile.class, "name")) {
			throw new DAOException(StatusCodes.DAO_NAME_TOO_LONG, "User file name too long");
		}
		if(entity.getType().length() > DAOUtil.columnLengthFor(UserFile.class, "type")) {
			throw new DAOException(StatusCodes.DAO_TYPE_TOO_LONG, "User file type too long");
		}
		if(entity.getContent().length() > DAOUtil.columnLengthFor(UserFile.class, "content")) {
			throw new DAOException(StatusCodes.DAO_CONTENT_TOO_LONG, "User file content too long");
		}
		if(entity.getUserId().length() > DAOUtil.columnLengthFor(UserFile.class, "userId")) {
			throw new DAOException(StatusCodes.DAO_USER_ID_TOO_LONG, "User identifier too long");
		}
		entity.setCreated(new Date());
		super.create(entity);
	}
	
	@Override
	public void save(UserFile entity) throws DAOException {
		if(entity.getName() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file name is null");
		}
		if(entity.getType() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file type is null");
		}
		if(entity.getContent() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User file content is null");
		}
		if(entity.getUserId() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "User identifier is null");
		}
		if(entity.getCreated() == null) {
			throw new DAOException(StatusCodes.SERVER_ERROR, "Created date is null");
		}
		if(entity.getName().length() > DAOUtil.columnLengthFor(UserFile.class, "name")) {
			throw new DAOException(StatusCodes.DAO_NAME_TOO_LONG, "User file name too long");
		}
		if(entity.getType().length() > DAOUtil.columnLengthFor(UserFile.class, "type")) {
			throw new DAOException(StatusCodes.DAO_TYPE_TOO_LONG, "User file type too long");
		}
		if(entity.getContent().length() > DAOUtil.columnLengthFor(UserFile.class, "content")) {
			throw new DAOException(StatusCodes.DAO_CONTENT_TOO_LONG, "User file content too long");
		}
		if(entity.getUserId().length() > DAOUtil.columnLengthFor(UserFile.class, "userId")) {
			throw new DAOException(StatusCodes.DAO_USER_ID_TOO_LONG, "User identifier too long");
		}
//		if(!exists(entity.getUserId(), entity.getName())) {
//			throw new DAOException(StatusCodes.DAO_DOESNT_EXIST, "Such file doesn't exist");
//		}
		super.save(entity);
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
