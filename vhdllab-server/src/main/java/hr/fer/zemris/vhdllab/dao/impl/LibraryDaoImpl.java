package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.LibraryDao;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Library;

import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link LibraryDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class LibraryDaoImpl extends AbstractEntityDao<Library> implements
        LibraryDao {

    /**
     * Default constructor.
     */
    public LibraryDaoImpl() {
        super(Library.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.dao.LibraryDAO#findByName(hr.fer.zemris.vhdllab
     * .entities.Caseless)
     */
    @Override
    public Library findByName(Caseless name) {
        Validate.notNull(name, "Name can't be null");
        String query = "select l from Library as l where l.name = ?1 order by l.id";
        return findUniqueResult(query, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.LibraryDAO#getAll()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Library> getAll() {
        String query = "select l from Library as l order by l.id";
        return getJpaTemplate().find(query);
    }

}
