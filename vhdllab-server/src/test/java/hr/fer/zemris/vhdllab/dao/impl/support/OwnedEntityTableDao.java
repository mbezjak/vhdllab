package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.impl.AbstractOwnedEntityDao;

/**
 * Used to test persistence of {@link OwnedEntityTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class OwnedEntityTableDao extends
        AbstractOwnedEntityDao<OwnedEntityTable> {

    public OwnedEntityTableDao() {
        super(OwnedEntityTable.class);
    }

}
