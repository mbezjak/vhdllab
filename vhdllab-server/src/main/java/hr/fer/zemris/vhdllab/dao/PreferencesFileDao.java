package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link PreferencesFile} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface PreferencesFileDao extends EntityDao<PreferencesFile> {

    /**
     * Finds all preferences files whose owner is specified user. Return value
     * will never be <code>null</code>, although it can be an empty list.
     * 
     * @param userId
     *            owner of preferences files
     * @return list of user's preferences files
     */
    List<PreferencesFile> findByUser(String user);

}
