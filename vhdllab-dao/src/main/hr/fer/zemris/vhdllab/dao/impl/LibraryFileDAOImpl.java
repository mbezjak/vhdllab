package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a default implementation of {@link LibraryFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class LibraryFileDAOImpl extends AbstractEntityDAO<LibraryFile>
		implements LibraryFileDAO {

	/**
	 * Sole constructor.
	 */
	public LibraryFileDAOImpl() {
		super(LibraryFile.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.LibraryFileDAO#exists(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Long libraryId, String name) throws DAOException {
		checkParameters(libraryId, name);
		String namedQuery = LibraryFile.FIND_BY_NAME_QUERY;
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", libraryId);
		params.put("name", name);
		return existsEntity(namedQuery, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.LibraryFileDAO#findByName(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public LibraryFile findByName(Long libraryId, String name)
			throws DAOException {
		checkParameters(libraryId, name);
		String namedQuery = LibraryFile.FIND_BY_NAME_QUERY;
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", libraryId);
		params.put("name", name);
		return findSingleEntity(namedQuery, params);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(Long libraryId, String name) {
		if (libraryId == null) {
			throw new NullPointerException("Library identifier cant be null");
		}
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

}
