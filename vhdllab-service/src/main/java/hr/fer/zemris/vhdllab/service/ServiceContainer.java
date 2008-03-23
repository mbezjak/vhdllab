package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.dao.DAOContainer;
import hr.fer.zemris.vhdllab.server.ServerContainer;
import hr.fer.zemris.vhdllab.service.impl.FileManagerImpl;
import hr.fer.zemris.vhdllab.service.impl.LibraryFileManagerImpl;
import hr.fer.zemris.vhdllab.service.impl.LibraryManagerImpl;
import hr.fer.zemris.vhdllab.service.impl.ProjectManagerImpl;
import hr.fer.zemris.vhdllab.service.impl.UserFileManagerImpl;
import hr.fer.zemris.vhdllab.service.impl.VHDLLabServiceManager;

import org.picocontainer.MutablePicoContainer;

/**
 * Contains every manager implementations of service tier for easy instantiation
 * and retrieval.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class ServiceContainer extends ServerContainer {

	/**
	 * Constructs a service container.
	 */
	public ServiceContainer(DAOContainer container) {
		super(container);
		MutablePicoContainer c = getContainer();
		c.addComponent(FileManagerImpl.class);
		c.addComponent(ProjectManagerImpl.class);
		c.addComponent(LibraryManagerImpl.class);
		c.addComponent(LibraryFileManagerImpl.class);
		c.addComponent(UserFileManagerImpl.class);
		c.addComponent(VHDLLabServiceManager.class);
	}

	/**
	 * Returns an implementation of {@link ProjectManager}. Repeated invocation
	 * of this method will return the same object instance. Returned value will
	 * never be <code>null</code>.
	 * 
	 * @return an implementation of {@link ProjectManager}
	 */
	public ProjectManager getProjectManager() {
		return getContainer().getComponent(ProjectManager.class);
	}

	/**
	 * Returns an implementation of {@link FileManager}. Repeated invocation of
	 * this method will return the same object instance. Returned value will
	 * never be <code>null</code>.
	 * 
	 * @return an implementation of {@link FileManager}
	 */
	public FileManager getFileManager() {
		return getContainer().getComponent(FileManager.class);
	}

	/**
	 * Returns an implementation of {@link LibraryManager}. Repeated invocation
	 * of this method will return the same object instance. Returned value will
	 * never be <code>null</code>.
	 * 
	 * @return an implementation of {@link LibraryManager}
	 */
	public LibraryManager getLibraryManager() {
		return getContainer().getComponent(LibraryManager.class);
	}

	/**
	 * Returns an implementation of {@link LibraryFileManager}. Repeated
	 * invocation of this method will return the same object instance. Returned
	 * value will never be <code>null</code>.
	 * 
	 * @return an implementation of {@link LibraryFileManager}
	 */
	public LibraryFileManager getLibraryFileManager() {
		return getContainer().getComponent(LibraryFileManager.class);
	}

	/**
	 * Returns an implementation of {@link UserFileManager}. Repeated
	 * invocation of this method will return the same object instance. Returned
	 * value will never be <code>null</code>.
	 * 
	 * @return an implementation of {@link UserFileManager}
	 */
	public UserFileManager getUserFileManager() {
		return getContainer().getComponent(UserFileManager.class);
	}

	/**
	 * Returns an implementation of {@link ServiceManager}. Repeated invocation
	 * of this method will return the same object instance. Returned value will
	 * never be <code>null</code>.
	 * 
	 * @return an implementation of {@link ServiceManager}
	 */
	public ServiceManager getServiceManager() {
		return getContainer().getComponent(ServiceManager.class);
	}

}
