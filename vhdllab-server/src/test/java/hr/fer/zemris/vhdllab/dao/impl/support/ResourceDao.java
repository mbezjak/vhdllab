package hr.fer.zemris.vhdllab.dao.impl.support;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.AbstractEntityDao;
import hr.fer.zemris.vhdllab.entities.Resource;

/**
 * Used to test persistence of {@link Resource}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ResourceDao extends AbstractEntityDao<ResourceTable> implements
        EntityDao<ResourceTable> {

    public ResourceDao() {
        super(ResourceTable.class);
    }

}
