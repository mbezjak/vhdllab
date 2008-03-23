package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Project;

import java.util.List;

/**
 * This interface extends {@link EntityDAO} to define extra methods for
 * {@link Project} model.
 * 
 * @author Miro Bezjak
 */
public interface ProjectDAO extends EntityDAO<Project> {

	/**
	 * Returns <code>true</code> if a project with specified
	 * <code>userId</code> and <code>name</code> exists.
	 * 
	 * @param userId
	 *            owner of a project
	 * @param name
	 *            a name of a project
	 * @return <code>true</code> if such project exists; <code>false</code>
	 *         otherwise
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	boolean exists(String userId, String name) throws DAOException;

	/**
	 * Finds all projects whose <code>userId</code> and <code>name</code>
	 * are specified by parameters. Return value will never be <code>null</code>.
	 * A {@link DAOException} will be thrown if such project doesn't exist.
	 * 
	 * @param userId
	 *            owner of project
	 * @param name
	 *            a name of a project
	 * @return a project
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	Project findByName(String userId, String name) throws DAOException;

	/**
	 * Finds all projects whose owner is specified user. Return value will never
	 * be <code>null</code>, although it can be an empty list.
	 * 
	 * @param userId
	 *            owner of projects
	 * @return list of user's projects
	 * @throws DAOException
	 *             if exceptional condition occurs
	 */
	List<Project> findByUser(String userId) throws DAOException;

}
