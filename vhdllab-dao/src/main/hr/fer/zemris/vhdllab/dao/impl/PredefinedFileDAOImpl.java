package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.entities.File;

import java.util.List;

/**
 * This is a default implementation of {@link PredefinedFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class PredefinedFileDAOImpl implements PredefinedFileDAO {

	private java.io.File basedir;

	/**
	 * Constructs a predefined file dao that points to specified predefined
	 * directory.
	 * 
	 * @param predefindDir
	 *            a directory that contains all predefined files
	 * @throws NullPointerException
	 *             if <code>predefinedDir</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified predefined directory doesn't exist or is not a
	 *             directory
	 */
	public PredefinedFileDAOImpl(String predefindDir) {
		if (predefindDir == null) {
			throw new NullPointerException("Predefined directory cant be null");
		}
		java.io.File basedir = new java.io.File(predefindDir);
		if (!basedir.exists()) {
			throw new IllegalArgumentException(
					"Predefined directory doesnt exist: " + basedir);
		}
		if (!basedir.isDirectory()) {
			throw new IllegalArgumentException(
					"Specified predefined directory doesnt exist: " + basedir);
		}
		this.basedir = basedir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.PredefinedFileDAO#load(java.lang.String)
	 */
	@Override
	public File load(String name) throws DAOException {
		name = chechForMaliciousPath(name);
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.PredefinedFileDAO#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String name) throws DAOException {
		name = chechForMaliciousPath(name);
		// TODO Auto-generated method stub
		return false;
	}

	private String chechForMaliciousPath(String name) throws DAOException {
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
		if (name.contains("./") || name.contains("../")) {
			throw new DAOException("Name contains illegal characters: " + name);
		}
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.dao.PredefinedFileDAO#getAll()
	 */
	@Override
	public List<File> getAll() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
