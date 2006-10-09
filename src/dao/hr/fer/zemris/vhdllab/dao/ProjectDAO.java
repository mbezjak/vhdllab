package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>Project</code>.
 */
public interface ProjectDAO {
	
	/**
	 * Retrieves project with specified identifier. An exception will be thrown if
	 * project with specified identifier does not exists.
	 * @param id indentifier of a project.
	 * @return a project with specified identifier.
	 * @throws DAOException if exceptional condition occurs.
	 */
	Project load(Long id) throws DAOException;
	/**
	 * Saves (or updates) a project.
	 * @param project a project that will be saved (or updated).
	 * @throws DAOException if exceptional condition occurs.
	 */
	void save(Project project) throws DAOException;
	/**
	 * Deletes a project with specified identifier.
	 * @param projectId indentifier of a project.
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(Long projectId) throws DAOException;
	/**
	 * Finds all projects whose owner is specified user. Return value will
	 * never be <code>null</code>, although it can be an empty list.
	 * @param userId ownerId of project
	 * @return list of user's projects
	 * @throws DAOException if exceptional condition occurs.
	 */
	List<Project> findByUser(Long userId) throws DAOException;
	/**
	 * Check if a project with specified identifier exists.
	 * @param projectId indentifier of a project.
	 * @return <code>true</code> if such project exists; <code>false</code> otherwise.
	 * @throws DAOException if exceptional condition occurs.
	 */
	boolean exists(Long projectId) throws DAOException;
}
