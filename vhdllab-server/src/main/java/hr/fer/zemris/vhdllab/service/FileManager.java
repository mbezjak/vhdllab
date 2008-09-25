package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * This interface extends {@link EntityManager} to define extra methods for
 * {@link File} model.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface FileManager extends EntityManager<File> {

	/**
	 * Returns <code>true</code> if specified project contains a file with
	 * given <code>name</code> or <code>false</code> otherwise.
	 * 
	 * @param projectId
	 *            identifier of the project
	 * @param name
	 *            name of a file
	 * @return <code>true</code> if such file exists; <code>false</code>
	 *         otherwise
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	boolean exists(Integer projectId, Caseless name) throws ServiceException;

	/**
	 * Returns a file with specified project identifier and file name. Return
	 * value will never be <code>null</code>. A {@link ServiceException} will be
	 * thrown if such file doesn't exist.
	 * 
	 * @param projectId
	 *            a project identifier
	 * @param name
	 *            a name of a file
	 * @return a file with specified project identifier and file name
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	File findByName(Integer projectId, Caseless name) throws ServiceException;

}
