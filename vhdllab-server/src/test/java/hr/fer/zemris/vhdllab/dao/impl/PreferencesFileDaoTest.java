package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.PreferencesFileDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link PreferencesFileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class PreferencesFileDaoTest extends AbstractDaoSupport {

    @Autowired
    private PreferencesFileDao dao;
    private PreferencesFile entity;

    @Before
    public void initEachTest() {
        entity = new PreferencesFile();
    }

    /**
     * Data can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void dataIsNull() {
        entity.setName("name");
        entity.setUserId("userId");
        entity.setData(null);
        dao.persist(entity);
    }

    /**
     * UserId and name are unique.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void uniqueUserIdAndName() {
        String userId = "userId";
        String name = "name";
        entity.setName(name);
        entity.setUserId(userId);
        entity.setData("data");
        dao.persist(entity);

        entity = new PreferencesFile();
        entity.setName(name);
        entity.setUserId(userId);
        entity.setData("new data");
        dao.persist(entity);
    }

}
