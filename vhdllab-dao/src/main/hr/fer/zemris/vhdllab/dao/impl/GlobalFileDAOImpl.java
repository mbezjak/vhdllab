package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.entities.GlobalFile;

import java.util.List;

/**
 * This is a default implementation of {@link GlobalFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 27/9/2007
 */
public class GlobalFileDAOImpl extends AbstractEntityDAO<GlobalFile> implements
		GlobalFileDAO {

	/**
	 * Sole constructor.
	 */
	public GlobalFileDAOImpl() {
		super(GlobalFile.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String name) throws DAOException {
		checkParameter(name);
		String namedQuery = GlobalFile.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "name" };
		Object[] paramValues = new Object[] { name };
		return existsEntity(namedQuery, paramNames, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#findByName(java.lang.String)
	 */
	@Override
	public GlobalFile findByName(String name) throws DAOException {
		checkParameter(name);
		String namedQuery = GlobalFile.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "name" };
		Object[] paramValues = new Object[] { name };
		return findSingleEntity(namedQuery, paramNames, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.GlobalFileDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalFile> getAll() throws DAOException {
		String namedQuery = GlobalFile.GET_ALL_QUERY;
		String[] paramNames = new String[] {};
		Object[] paramValues = new Object[] {};
		return findEntityList(namedQuery, paramNames, paramValues);
	}
	
	/**
	 * Throws {@link NullPointerException} if parameter is <code>null</code>.
	 */
	private void checkParameter(String name) {
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

}
