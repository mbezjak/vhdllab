package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.LibraryManager;
import hr.fer.zemris.vhdllab.service.ProjectManager;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.service.UserFileManager;

/**
 * A default implementation of {@link ServiceManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class VHDLLabServiceManager implements ServiceManager {

	/**
	 * A project manager.
	 */
	private final ProjectManager projectManager;
	/**
	 * A file manager.
	 */
	private final FileManager fileManager;
	/**
	 * A library manager.
	 */
	private final LibraryManager libraryManager;
	/**
	 * A library file manager.
	 */
	private final LibraryFileManager libraryFileManager;
	/**
	 * A user file manager.
	 */
	private final UserFileManager userFileManager;

	/**
	 * Sole constructor.
	 * 
	 * @param projectManager
	 *            a project manager
	 * @param fileManager
	 *            a file manager
	 * @param libraryManager
	 *            a library manager
	 * @param libraryFileManager
	 *            a library file manager
	 * @param userFileManager
	 *            a user file manager
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public VHDLLabServiceManager(ProjectManager projectManager,
			FileManager fileManager, LibraryManager libraryManager,
			LibraryFileManager libraryFileManager,
			UserFileManager userFileManager) {
		if (projectManager == null) {
			throw new NullPointerException("Project manager cant be null");
		}
		if (fileManager == null) {
			throw new NullPointerException("File manager cant be null");
		}
		if (libraryManager == null) {
			throw new NullPointerException("Library manager cant be null");
		}
		if (libraryFileManager == null) {
			throw new NullPointerException("Library file manager cant be null");
		}
		if (userFileManager == null) {
			throw new NullPointerException("User file manager cant be null");
		}
		this.projectManager = projectManager;
		this.fileManager = fileManager;
		this.libraryManager = libraryManager;
		this.libraryFileManager = libraryFileManager;
		this.userFileManager = userFileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getProjectManager()
	 */
	@Override
	public ProjectManager getProjectManager() {
		return projectManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getFileManager()
	 */
	@Override
	public FileManager getFileManager() {
		return fileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getLibraryManager()
	 */
	@Override
	public LibraryManager getLibraryManager() {
		return libraryManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getLibraryFileManager()
	 */
	@Override
	public LibraryFileManager getLibraryFileManager() {
		return libraryFileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getUserFileManager()
	 */
	@Override
	public UserFileManager getUserFileManager() {
		return userFileManager;
	}

}
