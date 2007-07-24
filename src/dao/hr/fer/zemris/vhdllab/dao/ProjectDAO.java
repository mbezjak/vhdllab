package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>Project</code>.
 */
public interface ProjectDAO {

	/**
	 * Retrieves project with specified identifier. An exception will be thrown
	 * if project with specified identifier does not exists.
	 * 
	 * @param id
	 *            identifier of a project.
	 * @return a project with specified identifier.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	Project load(Long id) throws DAOException;

	/**
	 * Saves (or updates) a project. Project must also have constraints as
	 * described by annotations in a {@link Project} model.
	 * 
	 * @param project
	 *            a project that will be saved (or updated).
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void save(Project project) throws DAOException;

	/**
	 * Deletes a project. If project does not exists then this method will throw
	 * <code>DAOException</code>.
	 * 
	 * @param project
	 *            a project to delete
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	void delete(Project project) throws DAOException;

	/**
	 * Check if a project with specified identifier exists.
	 * 
	 * @param projectId
	 *            identifier of a project.
	 * @return <code>true</code> if such project exists; <code>false</code>
	 *         otherwise.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(Long projectId) throws DAOException;

	/**
	 * Check if a project with specified <code>ownerId</code> and
	 * <code>projectName</code> exists.
	 * 
	 * @param ownerId
	 *            owner of project
	 * @param projectName
	 *            a name of a project
	 * @return <code>true</code> if such project exists; <code>false</code>
	 *         otherwise.
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	boolean exists(String ownerId, String projectName) throws DAOException;

	/**
	 * Finds all projects whose owner is specified user. Return value will never
	 * be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            ownerId of project
	 * @return list of user's projects
	 * @throws DAOException
	 *             if exceptional condition occurs.
	 */
	List<Project> findByUser(String userId) throws DAOException;

}
