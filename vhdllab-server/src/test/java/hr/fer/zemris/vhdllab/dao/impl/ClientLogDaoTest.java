package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.entity.FileType;

import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link ClientLogDaoImpl}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ClientLogDaoTest extends AbstractDaoSupport {

    @Autowired
    private ClientLogDao dao;
    private ClientLog entity;

    @Before
    public void initEachTest() {
        entity = new ClientLog();
    }

    /**
     * Type must be null.
     */
    @Test(expected = InvalidStateException.class)
    public void typeNotNull() {
        entity.setName("name");
        entity.setData("data");
        entity.setType(FileType.SOURCE);
        dao.persist(entity);
    }

    /**
     * Name is unique.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void uniqueName() {
        entity.setName("name");
        entity.setData("data");
        dao.persist(entity);
        entity = new ClientLog();
        entity.setName("name");
        entity.setData("new data");
        dao.persist(entity);
    }
    
}
