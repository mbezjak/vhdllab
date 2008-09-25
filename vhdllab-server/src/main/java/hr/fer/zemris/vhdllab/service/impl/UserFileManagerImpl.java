package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.preferences.global.PreferencesParser;
import hr.fer.zemris.vhdllab.preferences.global.Property;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.UserFileManager;

import java.util.List;

/**
 * This is a default implementation of {@link UserFileManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class UserFileManagerImpl extends AbstractEntityManager<UserFile>
        implements UserFileManager {

    /**
     * Constructor.
     * 
     * @param dao
     *            a data access object for an entity
     * @throws NullPointerException
     *             if <code>dao</code> is <code>null</code>
     */
    public UserFileManagerImpl(UserFileDAO dao) {
        super(dao);
    }

    /**
     * Returns a data access object of same type as constructor accepts it.
     * 
     * @return a data access object
     */
    private UserFileDAO getDAO() {
        /*
         * Can cast because sole constructor accepts object of this type.
         */
        return (UserFileDAO) dao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.UserFileManager#exists(java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean exists(Caseless userId, Caseless name) throws ServiceException {
        try {
            return getDAO().exists(userId, name);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.UserFileManager#findByName(java.lang.String
     * , java.lang.String)
     */
    @Override
    public UserFile findByName(Caseless userId, Caseless name)
            throws ServiceException {
        UserFile file = null;
        if (exists(userId, name)) {
            try {
                file = getDAO().findByName(userId, name);
            } catch (DAOException e) {
                e.printStackTrace();
                throw new ServiceException(StatusCodes.DAO_DOESNT_EXIST, e);
            }
        } else {
            ServiceContainer container = ServiceContainer.instance();
            Library lib = container.getLibraryManager().findByName(new Caseless("preferences"));
            LibraryFile globalFile = container.getLibraryFileManager().findByName(lib.getId(), name);
            file = synchronizeUserFile(globalFile, userId);
        }
        return file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.UserFileManager#findByUser(java.lang.String
     * )
     */
    @Override
    public List<UserFile> findByUser(Caseless userId) throws ServiceException {
        try {
            List<UserFile> files = getDAO().findByUser(userId);
            ServiceContainer container = ServiceContainer.instance();
            Library lib = container.getLibraryManager().findByName(new Caseless("preferences"));
            for (LibraryFile globalFile : lib.getFiles()) {
                boolean found = false;
                for (UserFile userFile : files) {
                    if (userFile.getName().equals(
                            globalFile.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    UserFile uf = synchronizeUserFile(globalFile, userId);
                    files.add(uf);
                }
            }
            return files;
        } catch (DAOException e) {
            e.printStackTrace();
            throw new ServiceException(StatusCodes.DAO_DOESNT_EXIST, e);
        }
    }

    private UserFile synchronizeUserFile(LibraryFile file, Caseless userId)
            throws ServiceException {
        Property property = PreferencesParser.parseProperty(file.getData());
        String data = property.getData().getDefaultValue();
        UserFile uf = new UserFile(userId, file.getName(), data);
        save(uf);
        return uf;
    }

}
