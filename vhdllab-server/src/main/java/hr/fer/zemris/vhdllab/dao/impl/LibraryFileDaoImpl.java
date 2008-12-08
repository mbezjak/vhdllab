package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.LibraryFileDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.LibraryFile;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link LibraryFileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileDaoImpl extends AbstractEntityDao<LibraryFile>
        implements LibraryFileDao {

    /**
     * Default constructor.
     */
    public LibraryFileDaoImpl() {
        super(LibraryFile.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(LibraryFile entity) {
        /*
         * If file and it's library are persisted and then a file attempts to be
         * deleted, all in the same session (EntityManager), file needs to be
         * removed from library before deleting it. Check
         * LibraryFileDAOImplTest#delete2() test to see an example.
         */
        entity.getLibrary().removeFile(entity);
        super.delete(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.LibraryFileDAO#findByName(java.lang.Integer,
     * hr.fer.zemris.vhdllab.entities.Caseless)
     */
    @Override
    public LibraryFile findByName(Integer libraryId, Caseless name) {
        Validate.notNull(libraryId, "Library identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select f from LibraryFile as f where f.library.id = ?1 and f.name = ?2 order by f.id";
        return findUniqueResult(query, libraryId, name);
    }

}
