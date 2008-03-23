package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * This is a default implementation of {@link FileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 27/9/2007
 */
public final class FileDAOImpl extends AbstractEntityDAO<File> implements
		FileDAO {

	/**
	 * Sole constructor.
	 */
	public FileDAOImpl() {
		super(File.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#exists(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Long projectId, String name) throws DAOException {
		checkParameters(projectId, name);
		String namedQuery = File.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "id", "name" };
		Object[] paramValues = new Object[] { projectId, name };
		return existsEntity(namedQuery, paramNames, paramValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.FileDAO#findByName(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public File findByName(Long projectId, String name) throws DAOException {
		checkParameters(projectId, name);
		String namedQuery = File.FIND_BY_NAME_QUERY;
		String[] paramNames = new String[] { "id", "name" };
		Object[] paramValues = new Object[] { projectId, name };
		return findSingleEntity(namedQuery, paramNames, paramValues);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(Long projectId, String name) {
		if (projectId == null) {
			throw new NullPointerException("Project identifier cant be null");
		}
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

}
