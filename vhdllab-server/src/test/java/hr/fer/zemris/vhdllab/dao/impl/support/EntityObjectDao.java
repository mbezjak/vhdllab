package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;
import hr.fer.zemris.vhdllab.entities.EntityObject;

/**
 * Used to test persistence of {@link EntityObject}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObjectDao extends AbstractEntityDao<EntityObjectTable>
        implements EntityDao<EntityObjectTable> {

    public EntityObjectDao() {
        super(EntityObjectTable.class);
    }

}
