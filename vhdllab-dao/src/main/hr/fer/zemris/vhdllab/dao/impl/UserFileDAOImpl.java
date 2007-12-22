package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.List;

/**
 * This is a default implementation of {@link UserFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 27/9/2007
 */
public class UserFileDAOImpl extends AbstractEntityDAO<UserFile> implements
		UserFileDAO {

	/**
	 * Sole constructor.
	 */
	public UserFileDAOImpl() {
		super(UserFile.class);
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
		String[] paramNames = new String[] { "userId", "name" };
		Object[] paramValues = new Object[] { userId, name };
		return existsEntity(namedQuery, paramNames, paramValues);
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
		String[] paramNames = new String[] { "userId", "name" };
		Object[] paramValues = new Object[] { userId, name };
		return findSingleEntity(namedQuery, paramNames, paramValues);
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
		String[] paramNames = new String[] { "userId" };
		Object[] paramValues = new Object[] { userId };
		return findEntityList(namedQuery, paramNames, paramValues);
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
