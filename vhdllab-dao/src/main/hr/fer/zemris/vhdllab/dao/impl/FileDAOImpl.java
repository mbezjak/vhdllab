package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.entities.File;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a default implementation of {@link FileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class FileDAOImpl extends AbstractDatabaseEntityDAO<File> implements
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
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", projectId);
		params.put("name", name);
		return existsEntity(namedQuery, params);
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
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", projectId);
		params.put("name", name);
		return findSingleEntity(namedQuery, params);
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
