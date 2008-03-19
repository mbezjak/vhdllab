package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;

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

	/**
	 * Generates a <code>VHDL</code> code for a specified <code>file</code>
	 * (if necessary) and returns a result.
	 * 
	 * @param file
	 *            a file for which VHDL must be generated
	 * @return <code>VHDL</code> generation result for specified file
	 * @throws ServiceException
	 *             if any exception occurs
	 */
	VHDLGenerationResult generateVHDL(File file) throws ServiceException;

}
