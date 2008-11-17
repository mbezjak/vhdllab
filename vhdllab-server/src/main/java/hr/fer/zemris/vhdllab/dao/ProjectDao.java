package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link Project} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface ProjectDao extends EntityDao<Project> {

    /**
     * Finds a project whose <code>userId</code> and <code>name</code> are
     * specified by parameters. <code>null</code> value will be returned if such
     * project doesn't exist.
     * 
     * @param userId
     *            owner of project
     * @param name
     *            a name of a project
     * @throws NullPointerException
     *             if any parameter is <code>null</code>
     * @return specified project or <code>null</code> if such project doesn't
     *         exist
     */
    Project findByName(Caseless userId, Caseless name);

    /**
     * Finds all projects whose owner is specified user. Return value will never
     * be <code>null</code>, although it can be an empty list.
     * 
     * @param userId
     *            owner of projects
     * @return list of user's projects
     */
    List<Project> findByUser(Caseless userId);

}
