package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.entities.Library;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * This is a default implementation of {@link LibraryDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class LibraryDAOImpl implements LibraryDAO {

	public LibraryDAOImpl(File basedir) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean exists(String name) throws DAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Library findByName(String name) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Library> getAll() throws DAOException {
		return Collections.emptySet();
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
	public Library load(Long id) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Library entity) throws DAOException {
		// TODO Auto-generated method stub
		
	}

}
