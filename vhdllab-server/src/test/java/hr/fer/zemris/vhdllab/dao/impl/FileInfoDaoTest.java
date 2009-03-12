package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.FileInfoDao;
import hr.fer.zemris.vhdllab.dao.impl.support.FileInfoTable;
import hr.fer.zemris.vhdllab.entity.FileType;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link FileInfoDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoDaoTest extends AbstractDaoSupport {

    @Resource(name = "fileInfoDao")
    private EntityDao<FileInfoTable> dao;
    private FileInfoTable entity;

    @Before
    public void initEachTest() {
        entity = new FileInfoTable();
    }

    /**
     * Data can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void dataIsNull() {
        entity.setName("name");
        entity.setData(null);
        dao.persist(entity);
    }

    /**
     * Data is too long.
     */
    @Test(expected = InvalidStateException.class)
    public void dataTooLong() {
        entity.setName("name");
        entity.setData(RandomStringUtils.randomAlphabetic(16000001));
        dao.persist(entity);
    }

    /**
     * Type can't be updated.
     */
    @Test
    public void typeNotUpdateable() {
        entity.setName("name");
        entity.setData("data");
        entity.setType(FileType.SOURCE);
        dao.persist(entity);
        FileType newType = FileType.AUTOMATON;
        entity.setType(newType);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();
        FileInfoTable loaded = (FileInfoTable) entityManager.createQuery(
                "select e from FileInfoTable e").getSingleResult();
        assertFalse(newType.equals(loaded.getType()));
    }

}
