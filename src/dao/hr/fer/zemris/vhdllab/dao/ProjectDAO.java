package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.model.Project;

import java.util.List;

/**
 * This interface defines methods for persisting model <code>Project</code>.
 */
public interface ProjectDAO {
	
	/**
	 * Returns a <code>Project</code> that has project ID equal to <code>id</code>.
	 * @param id a project ID.
	 * @return a <code>Project</code> that has project ID equal to <code>id</code>. 
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
	 * Deletes a project with <code>projectID</code>.
	 * @param projectID a project ID.
	 * @throws DAOException if exceptional condition occurs.
	 */
	void delete(Long projectID) throws DAOException;
	/**
	 * Returns a list of <code>Project</code> that have ownerID equal to <code>userID</code>.
	 * @param userID an ownerID of a <code>Project</code>.
	 * @return a list of <code>Project</code> that have ownerID equal to <code>userID</code>.
	 * @throws DAOException if exceptional condition occurs.
	 */
	List<Project> findByUser(Long userID) throws DAOException;
	
}
