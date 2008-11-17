package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link LibraryFile} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface LibraryFileDao extends EntityDao<LibraryFile> {

    /**
     * Finds a library file whose <code>libraryId</code> and <code>name</code>
     * are specified by parameters. <code>null</code> value will be returned if
     * such file doesn't exist.
     * 
     * @param libraryId
     *            a library identifier
     * @param name
     *            a name of a library file
     * @throws NullPointerException
     *             if any parameter is <code>null</code>
     * @return specified library file or <code>null</code> if such entity
     *         doesn't exist
     */
    LibraryFile findByName(Integer libraryId, Caseless name);

}
