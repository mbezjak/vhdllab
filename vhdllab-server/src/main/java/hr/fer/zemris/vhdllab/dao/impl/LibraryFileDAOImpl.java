package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
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
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#preDeleteAction(java.lang.Object)
	 */
	@Override
	protected void preDeleteAction(LibraryFile file) {
		/*
		 * If file and it's library are persisted and then a file attempts to be
		 * deleted, all in the same session (EntityManager), file needs to be
		 * removed from library before deleting it. Check
		 * LibraryFileDAOImplTest#delete2() test to see an example.
		 */
		file.getLibrary().removeFile(file);
		super.preDeleteAction(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.LibraryFileDAO#exists(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Integer libraryId, Caseless name) throws DAOException {
		checkParameters(libraryId, name);
        String query = "select f from LibraryFile as f where f.library.id = :id and f.name = :name order by f.id";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", libraryId);
		params.put("name", name);
		return existsEntity(query, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.LibraryFileDAO#findByName(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public LibraryFile findByName(Integer libraryId, Caseless name)
			throws DAOException {
		checkParameters(libraryId, name);
        String query = "select f from LibraryFile as f where f.library.id = :id and f.name = :name order by f.id";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("id", libraryId);
		params.put("name", name);
		return findSingleEntity(query, params);
	}

	/**
	 * Throws {@link NullPointerException} is any parameter is <code>null</code>.
	 */
	private void checkParameters(Integer libraryId, Caseless name) {
		if (libraryId == null) {
			throw new NullPointerException("Library identifier cant be null");
		}
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
	}

}
