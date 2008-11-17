package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.ResourceDao;
import hr.fer.zemris.vhdllab.dao.impl.support.ResourceTable;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ResourceDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ResourceDaoTest extends AbstractDaoSupport {

    @Resource(name = "resourceDao")
    private EntityDao<ResourceTable> dao;
    private ResourceTable resource;

    @Before
    public void initEachTest() {
        resource = new ResourceTable(NAME, DATA);
    }

    /**
     * Data of a file can be a part of update statement. Version is incremented
     * for each save.
     */
    @Test
    public void saveNewDataAndVersionIsIncremented() {
        dao.save(resource);
        assertEquals("version not 0 after creation.", Integer.valueOf(0),
                resource.getVersion());
        resource.setData(DATA_2);
        dao.save(resource);
        assertEquals("files not same after data was updated.", resource, dao
                .load(resource.getId()));
        assertEquals("version not 1 after first save.", Integer.valueOf(1),
                resource.getVersion());
    }

}
