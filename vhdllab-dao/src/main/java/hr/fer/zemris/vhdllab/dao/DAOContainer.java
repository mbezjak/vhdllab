package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.dao.impl.FileDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.LibraryDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.LibraryFileDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.ProjectDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.UserFileDAOImpl;
import hr.fer.zemris.vhdllab.server.ServerContainer;

import org.picocontainer.MutablePicoContainer;

/**
 * Contains DAO implementations of every entity for easy instantiation and
 * retrieval.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class DAOContainer extends ServerContainer {

    /**
     * Reference to single instance of DAO container (singleton pattern).
     */
    private static final DAOContainer INSTANCE = new DAOContainer();

    /**
     * Constructs a DAO container.
     */
    private DAOContainer() {
        super();
        MutablePicoContainer c = getContainer();
        c.addComponent(FileDAOImpl.class);
        c.addComponent(ProjectDAOImpl.class);
        c.addComponent(LibraryDAOImpl.class);
        c.addComponent(LibraryFileDAOImpl.class);
        c.addComponent(UserFileDAOImpl.class);
    }

    /**
     * Returns an instance of DAO container. Repeated invocation of this method
     * will return the same object instance. Return value will never be
     * <code>null</code>.
     *
     * @return an instance of DAO container
     */
    public static DAOContainer instance() {
        return INSTANCE;
    }

    /**
     * Returns an implementation of {@link ProjectDAO}. Repeated invocation of
     * this method will return the same object instance. Returned value will
     * never be <code>null</code>.
     *
     * @return an implementation of {@link ProjectDAO}
     */
    public ProjectDAO getProjectDAO() {
        return getContainer().getComponent(ProjectDAO.class);
    }

    /**
     * Returns an implementation of {@link FileDAO}. Repeated invocation of
     * this method will return the same object instance. Returned value will
     * never be <code>null</code>.
     *
     * @return an implementation of {@link FileDAO}
     */
    public FileDAO getFileDAO() {
        return getContainer().getComponent(FileDAO.class);
    }

    /**
     * Returns an implementation of {@link LibraryDAO}. Repeated invocation of
     * this method will return the same object instance. Returned value will
     * never be <code>null</code>.
     *
     * @return an implementation of {@link LibraryDAO}
     */
    public LibraryDAO getLibraryDAO() {
        return getContainer().getComponent(LibraryDAO.class);
    }

    /**
     * Returns an implementation of {@link LibraryFileDAO}. Repeated invocation
     * of this method will return the same object instance. Returned value will
     * never be <code>null</code>.
     *
     * @return an implementation of {@link LibraryFileDAO}
     */
    public LibraryFileDAO getLibraryFileDAO() {
        return getContainer().getComponent(LibraryFileDAO.class);
    }

    /**
     * Returns an implementation of {@link UserFileDAO}. Repeated invocation of
     * this method will return the same object instance. Returned value will
     * never be <code>null</code>.
     *
     * @return an implementation of {@link UserFileDAO}
     */
    public UserFileDAO getUserFileDAO() {
        return getContainer().getComponent(UserFileDAO.class);
    }

}
