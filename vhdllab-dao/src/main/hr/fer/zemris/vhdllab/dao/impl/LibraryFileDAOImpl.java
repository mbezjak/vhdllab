package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import java.io.File;

/**
 * This is a default implementation of {@link LibraryFileDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class LibraryFileDAOImpl implements LibraryFileDAO {

	public LibraryFileDAOImpl(File basedir) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean exists(Long libraryId, String name) throws DAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LibraryFile findByName(Long libraryId, String name)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(Long id) throws DAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LibraryFile load(Long id) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(LibraryFile entity) throws DAOException {
		// TODO Auto-generated method stub

	}

}
