package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;

/**
 * This is a default implementation of {@link FileManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class FileManagerImpl extends AbstractEntityManager<File>
		implements FileManager {

	/**
	 * Constructor.
	 * 
	 * @param dao
	 *            a data access object for an entity
	 * @throws NullPointerException
	 *             if <code>dao</code> is <code>null</code>
	 */
	public FileManagerImpl(FileDAO dao) {
		super(dao);
	}

	/**
	 * Returns a data access object of same type as constructor accepts it.
	 * 
	 * @return a data access object
	 */
	private FileDAO getDAO() {
		/*
		 * Can cast because sole constructor accepts object of this type.
		 */
		return (FileDAO) dao;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.service.impl.AbstractEntityManager#save(java.lang.Object)
	 */
	@Override
	public void save(File entity) throws ServiceException {
	    if (entity.getType().equals(FileType.SOURCE)) {
	        ServiceManager sm = ServiceContainer.instance().getServiceManager();
            try {
                sm.extractCircuitInterface(entity);
            } catch (ServiceException e) {
                if(e.getMessage().equals("Entity block is invalid.")) {
                    throw new ServiceException(
                            StatusCodes.SERVICE_ENTITY_AND_FILE_NAME_DONT_MATCH,
                            "Resource "
                                    + entity.getName()
                                    + " must have only one entity with the same name.");
                }
            }
	    }
	    super.save(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.FileManager#exists(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public boolean exists(Integer projectId, Caseless name) throws ServiceException {
		try {
			return getDAO().exists(projectId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.FileManager#findByName(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	public File findByName(Integer projectId, Caseless name) throws ServiceException {
		try {
			return getDAO().findByName(projectId, name);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
