package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This is a default implementation of {@link FileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class FileDAOImpl extends AbstractEntityDAO<File> implements
		FileDAO {

	/**
	 * A log instance.
	 */
	private static final Logger log = Logger.getLogger(FileDAOImpl.class);

	/**
	 * Sole constructor.
	 */
	public FileDAOImpl() {
		super(File.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDAO#save(java.lang.Object)
	 */
	@Override
	public void save(File file) throws DAOException {
		if (file == null) {
			throw new NullPointerException("File cant be null");
		}
		ServerConf conf = ServerConfParser.getConfiguration();
		if (!conf.containsFileType(file.getType())) {
			if (log.isInfoEnabled()) {
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
