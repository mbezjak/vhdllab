package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.EntityObjectDao;
import hr.fer.zemris.vhdllab.dao.impl.support.EntityObjectTable;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EntityObjectDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObjectDaoTest extends AbstractDaoSupport {

    @Resource(name = "entityObjectDao")
    private EntityDao<EntityObjectTable> dao;
    private EntityObjectTable entity;

    @Before
    public void initEachTest() {
        entity = new EntityObjectTable(NAME);
    }

    /**
     * Once file is persisted an ID is no longer null and version is 0.
     */
    @Test
    public void saveIdAndVersion() {
        assertNull("file has id set.", entity.getId());
        dao.save(entity);
        assertNotNull("file id wasn't set after creation.", entity.getId());
        assertEquals("version not 0 after creation.", Integer.valueOf(0),
                entity.getVersion());
    }

    /**
     * Save an entity, load it and see it they are the same then delete it.
     */
    @Test
    public void saveLoadAndDelete() {
        dao.save(entity);
        EntityObjectTable loadedEntity = dao.load(entity.getId());
        assertEquals("entity not equal after creating and loading it.", entity,
                loadedEntity);
        assertEquals("names are not same.", NAME, loadedEntity.getName());
        dao.delete(entity);
        assertNull("entity exists after it was deleted.", dao.load(entity
                .getId()));
    }

}
