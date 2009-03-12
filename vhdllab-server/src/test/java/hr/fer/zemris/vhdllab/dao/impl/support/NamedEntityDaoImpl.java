package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link NamedEntityTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class NamedEntityDaoImpl extends AbstractEntityDao<NamedEntityTable>
        implements EntityDao<NamedEntityTable> {

    public NamedEntityDaoImpl() {
        super(NamedEntityTable.class);
    }

}
