package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.ProjectInfoDao;
import hr.fer.zemris.vhdllab.dao.impl.support.ProjectInfoTable;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link ProjectInfoDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoDaoTest extends AbstractDaoSupport {

    @Resource(name = "projectInfoDao")
    private EntityDao<ProjectInfoTable> dao;
    private ProjectInfoTable entity;

    @Before
    public void initEachTest() {
        entity = new ProjectInfoTable();
    }

    /**
     * Type can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void typeIsNull() {
        entity.setName("name");
        entity.setType(null);
        dao.persist(entity);
    }

    /**
     * UserId can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void userIdIsNull() {
        entity.setName("name");
        entity.setType(ProjectType.USER);
        entity.setUserId(null);
        dao.persist(entity);
    }
    
    /**
     * UserId is too long.
     */
    @Test(expected = InvalidStateException.class)
    public void userIdTooLong() {
        entity.setName("name");
        entity.setType(ProjectType.USER);
        entity.setUserId(RandomStringUtils.randomAlphabetic(256));
        dao.persist(entity);
    }

    /**
     * UserId and Type can't be updated.
     */
    @Test
    public void userIdAndTypeNotUpdateable() {
        entity.setName("name");
        entity.setUserId("userId");
        entity.setType(ProjectType.USER);
        dao.persist(entity);
        ProjectType newType = ProjectType.PREFERENCES;
        String newUserId = "newUserId";
        entity.setType(newType);
        entity.setUserId(newUserId);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();
        ProjectInfoTable loaded = (ProjectInfoTable) entityManager.createQuery(
                "select e from ProjectInfoTable e").getSingleResult();
        assertFalse(newType.equals(loaded.getType()));
        assertFalse(newUserId.equals(loaded.getUserId()));
    }

}
