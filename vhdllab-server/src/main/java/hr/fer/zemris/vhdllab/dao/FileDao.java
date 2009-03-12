package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entity.File;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link File} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface FileDao extends EntityDao<File> {

    /**
     * Finds a file whose <code>projectId</code> and <code>name</code> are
     * specified by parameters. <code>null</code> value will be returned if such
     * file doesn't exist.
     * 
     * @param projectId
     *            a project identifier
     * @param name
     *            a name of a file
     * @throws NullPointerException
     *             if any parameter is <code>null</code>
     * @return specified file or <code>null</code> if such entity doesn't exist
     */
    File findByName(Integer projectId, String name);

}
