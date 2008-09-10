package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.ServiceException;

/**
 * This is a default implementation of {@link LibraryFileManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class LibraryFileManagerImpl extends
		AbstractEntityManager<LibraryFile> implements LibraryFileManager {

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public LibraryFileManagerImpl(LibraryFileDAO dao) {
		super(dao);
	}

	/**
	 * Returns a data access object of same type as constructor accepts it.
	 * 
	 * @return a data access object
	 */
	private LibraryFileDAO getDAO() {
		/*
		 * Can cast because sole constructor accepts object of this type.
		 */
		return (LibraryFileDAO) dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryFileManager#exists(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Long libraryId, String name) throws ServiceException {
		try {
			return getDAO().exists(libraryId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.LibraryFileManager#findByName(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public LibraryFile findByName(Long libraryId, String name)
			throws ServiceException {
		try {
			return getDAO().findByName(libraryId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
