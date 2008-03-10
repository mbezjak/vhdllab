package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
	 * A log instance.
	 */
	private static final Logger log = Logger.getLogger(LibraryFileDAOImpl.class);

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
		 * If file and it's project are persisted and then a file attempts to be
		 * deleted, all in the same session (EntityManager), file needs to be
		 * removed from project before deleting it. Check
		 * LibraryFileDAOImplTest#delete2() test to see an example.
		 */
		file.getLibrary().removeFile(file);
		super.preDeleteAction(file);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#save(java.lang.Object)
	 */
	@Override
	public void save(LibraryFile file) throws DAOException {
		if (file == null) {
			throw new NullPointerException("Library file cant be null");
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
