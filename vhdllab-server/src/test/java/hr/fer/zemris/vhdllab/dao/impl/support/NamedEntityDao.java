package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link NamedEntityTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class NamedEntityDao extends AbstractEntityDao<NamedEntityTable> {

    public NamedEntityDao() {
        super(NamedEntityTable.class);
    }

}
