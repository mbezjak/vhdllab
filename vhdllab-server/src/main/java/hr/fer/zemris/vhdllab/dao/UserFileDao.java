package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link UserFile} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface UserFileDao extends EntityDao<UserFile> {

    /**
     * Finds a user file whose <code>userId</code> and <code>name</code> are
     * specified by parameters. <code>null</code> value will be returned if such
     * file doesn't exist.
     * 
     * @param userId
     *            owner of a user file
     * @param name
     *            a name of a user file
     * @throws NullPointerException
     *             if any parameter is <code>null</code>
     * @return specified user file or <code>null</code> if such file doesn't
     *         exist
     */
    UserFile findByName(Caseless userId, Caseless name);

    /**
     * Finds all user files whose owner is specified user. Return value will
     * never be <code>null</code>, although it can be an empty list.
     * 
     * @param userId
     *            owner of a user file
     * @return list of user files
     */
    List<UserFile> findByUser(Caseless userId);

}
