package hr.fer.zemris.vhdllab.service;

/**
 * This is an interface representing a service manager. This interface defines
 * the communication between presentation and service tier.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface ServiceManager {

	/**
	 * Returns a project manager.
	 * 
	 * @return a project manager
	 */
	ProjectManager getProjectManager();

	/**
	 * Returns a file manager.
	 * 
	 * @return a file manager
	 */
	FileManager getFileManager();

	/**
	 * Returns a library manager.
	 * 
	 * @return a library manager
	 */
	LibraryManager getLibraryManager();

	/**
	 * Returns a library file manager.
	 * 
	 * @return a library file manager
	 */
	LibraryFileManager getLibraryFileManager();

	/**
	 * Returns a user file manager.
	 * 
	 * @return a user file manager
	 */
	UserFileManager getUserFileManager();

}
