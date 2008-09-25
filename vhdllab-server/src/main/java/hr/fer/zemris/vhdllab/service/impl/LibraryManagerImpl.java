package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.service.LibraryManager;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.util.List;

/**
 * This is a default implementation of {@link LibraryManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class LibraryManagerImpl extends AbstractEntityManager<Library>
		implements LibraryManager {

	/**
	 * A name of a predefined library.
	 */
	private static final Caseless PREDEFINED_LIBRARY_NAME = new Caseless("predefined");

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public LibraryManagerImpl(LibraryDAO dao) {
		super(dao);
	}

	/**
	 * Returns a data access object of same type as constructor accepts it.
	 * 
	 * @return a data access object
	 */
	private LibraryDAO getDAO() {
		/*
		 * Can cast because sole constructor accepts object of this type.
		 */
		return (LibraryDAO) dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryManager#exists(java.lang.String)
	 */
	@Override
	public boolean exists(Caseless name) throws ServiceException {
		try {
			return getDAO().exists(name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryManager#findByName(java.lang.String)
	 */
	@Override
	public Library findByName(Caseless name) throws ServiceException {
		try {
			return getDAO().findByName(name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryManager#getPredefinedLibrary()
	 */
	@Override
	public Library getPredefinedLibrary() throws ServiceException {
		return findByName(PREDEFINED_LIBRARY_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryManager#getAll()
	 */
	@Override
	public List<Library> getAll() throws ServiceException {
		try {
			return getDAO().getAll();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
