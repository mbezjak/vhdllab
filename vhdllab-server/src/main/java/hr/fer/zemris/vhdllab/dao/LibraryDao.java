package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link Library} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Transactional
public interface LibraryDao extends EntityDao<Library> {

    /**
     * Finds a library whose <code>name</code> is specified by parameter.
     * <code>null</code> value will be returned if such entity doesn't exist.
     * 
     * @param name
     *            a name of a library
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>
     * @return specified library or <code>null</code> if such entity doesn't
     *         exist
     */
    Library findByName(Caseless name);

    /**
     * Finds all defined libraries. Return value will never be <code>null</code>
     * although it can be empty list.
     * 
     * @return a all defined libraries
     */
    List<Library> getAll();

}
