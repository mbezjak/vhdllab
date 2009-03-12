package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;

/**
 * Used to test persistence of {@link BaseEntityTable}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class BaseEntityDao extends AbstractEntityDao<BaseEntityTable>
        implements EntityDao<BaseEntityTable> {

    public BaseEntityDao() {
        super(BaseEntityTable.class);
    }

}
