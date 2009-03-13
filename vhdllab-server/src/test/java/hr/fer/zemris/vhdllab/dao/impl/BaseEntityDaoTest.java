package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.BaseEntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.BaseEntityTable;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateSystemException;

/**
 * Tests for {@link BaseEntityDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class BaseEntityDaoTest extends AbstractDaoSupport {

    @Resource(name = "baseEntityDao")
    private EntityDao<BaseEntityTable> dao;
    private BaseEntityTable entity;

    @Before
    public void initEachTest() {
        entity = new BaseEntityTable();
    }

    /**
     * Once file is persisted an ID is no longer null and version is 0.
     */
    @Test
    public void persistIdAndVersion() {
        assertNull("id is set.", entity.getId());
        dao.persist(entity);
        assertNotNull("id wasn't set after creation.", entity.getId());
        assertEquals("version not 0 after creation.", Integer.valueOf(0),
                entity.getVersion());
    }

    /**
     * Save an entity, load it and see it they are the same then delete it.
     */
    @Test
    public void persistLoadAndDelete() {
        dao.persist(entity);
        BaseEntityTable loadedEntity = dao.load(entity.getId());
        assertEquals("id not same.", entity.getId(), loadedEntity.getId());
        assertEquals("version not same.", entity.getVersion(), loadedEntity
                .getVersion());
        dao.delete(entity);
        assertNull("entity exists after it was deleted.", dao.load(entity
                .getId()));
    }

    /**
     * Id is set.
     */
    @Test
    public void persistIdSet() {
        Integer id = 1000;
        entity.setId(id);
        dao.persist(entity);
        assertFalse("entity is persisted with specified id.", id.equals(entity
                .getId()));
        assertNotNull("entity not persisted.", dao.load(entity.getId()));
        assertNull("entity persisted with specified id.", dao.load(id));
    }

    /**
     * Version is set.
     */
    @Test
    public void persistVersionSet() {
        Integer version = 100;
        entity.setVersion(version);
        dao.persist(entity);
        BaseEntityTable baseEntity = dao.load(entity.getId());
        assertNotNull("entity not persisted.", baseEntity);
        assertEquals("entity version not persisted.", version, baseEntity
                .getVersion());
    }

    /**
     * Id not set.
     */
    @Test
    public void mergeIdNotSet() {
        BaseEntityTable merged = dao.merge(entity);
        assertTrue(entity.isNew());
        assertFalse(merged.isNew());
        assertNotNull(dao.load(merged.getId()));
    }

    /**
     * Version is incremented automatically.
     */
    @Test
    public void mergeVersionIncrement() {
        dao.persist(entity);
        assertEquals(Integer.valueOf(0), entity.getVersion());
        entity.setVersion(100);
        BaseEntityTable merged = dao.merge(entity);
        assertEquals(Integer.valueOf(1), merged.getVersion());
    }

    /**
     * Id can't be updated.
     */
    @Test(expected = HibernateSystemException.class)
    public void mergeIdCantBeUpdated() {
        dao.persist(entity);
        entity.setId(1000);
        dao.merge(entity);
    }

    /**
     * Detached entity passed to persist.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void detachedEntity() {
        dao.persist(entity);
        closeEntityManager();
        createEntityManager();
        dao.persist(entity);
    }

    /**
     * Detached entity passed to persist.
     */
    @Test
    public void detachedEntityUsingMerge() {
        dao.persist(entity);
        closeEntityManager();
        createEntityManager();
        dao.merge(entity); // no exception
    }

}
