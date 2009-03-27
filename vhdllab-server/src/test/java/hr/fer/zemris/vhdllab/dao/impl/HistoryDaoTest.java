package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.HistoryDao;
import hr.fer.zemris.vhdllab.dao.impl.support.HistoryTable;
import hr.fer.zemris.vhdllab.entity.History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link HistoryDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class HistoryDaoTest extends AbstractDaoSupport {

    public static final Date CREATED_ON;
    public static final Date DELETED_ON;
    public static final Date DELETED_ON_BEFORE_CREATED_ON;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat(
                DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        try {
            CREATED_ON = formatter.parse("2008-01-05");
            DELETED_ON = formatter.parse("2008-03-12");
            DELETED_ON_BEFORE_CREATED_ON = formatter.parse("2007-08-21");
        } catch (ParseException e) {
            throw new UnhandledException(e);
        }
    }

    @Resource(name = "historyDao")
    private EntityDao<HistoryTable> dao;
    private HistoryTable entity;
    private History history;

    @Before
    public void initEachTest() {
        history = new History();
        entity = new HistoryTable();
        entity.setHistory(history);
    }

    /**
     * Insert version can't be null.
     */
    @Test(expected = InvalidStateException.class)
    public void insertVersionIsNull() {
        history.setInsertVersion(null);
        dao.persist(entity);
    }

    /**
     * Update version can't be null.
     */
    @Test(expected = InvalidStateException.class)
    public void updateVersionIsNull() {
        history.setUpdateVersion(null);
        dao.persist(entity);
    }

    /**
     * Created on can't be null.
     */
    @Test(expected = InvalidStateException.class)
    public void createdOnIsNull() {
        history.setCreatedOn(null);
        dao.persist(entity);
    }

    /**
     * Insert version can't be null.
     */
    @Test(expected = InvalidStateException.class)
    public void insertVersionNegative() {
        history.setInsertVersion(-1);
        dao.persist(entity);
    }

    /**
     * Update version can't be null.
     */
    @Test(expected = InvalidStateException.class)
    public void updateVersionNegative() {
        history.setUpdateVersion(-1);
        dao.persist(entity);
    }

    /**
     * Deleted on must be after created on.
     */
    @Test(expected = InvalidStateException.class)
    public void deletedOnBeforeCreatedOn() {
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(DELETED_ON_BEFORE_CREATED_ON);
        dao.persist(entity);
    }

    /**
     * History is updated with deleted on date.
     */
    @Test
    public void updateDeletedOn() {
        history.setCreatedOn(CREATED_ON);
        dao.persist(entity);
        history.setDeletedOn(DELETED_ON);
        dao.merge(entity);
        entity = dao.load(entity.getId());
        assertEquals(DELETED_ON, entity.getHistory().getDeletedOn());
    }
    
    /**
     * Insert version, update version and created on can't be updated.
     */
    @Test
    public void userIdTypeAndCreatedOnNotUpdateable() {
        dao.persist(entity);
        Integer newInsertVersion = 12;
        Integer newUpdateVersion = 32;
        Date newCreatedOn = CREATED_ON;
        history.setInsertVersion(newInsertVersion);
        history.setUpdateVersion(newUpdateVersion);
        history.setCreatedOn(newCreatedOn);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();
        HistoryTable loaded = (HistoryTable) entityManager.createQuery(
                "select e from HistoryTable e").getSingleResult();
        History loadedHistory = loaded.getHistory();
        assertFalse(newInsertVersion.equals(loadedHistory.getInsertVersion()));
        assertFalse(newUpdateVersion.equals(loadedHistory.getUpdateVersion()));
        assertFalse(newCreatedOn.equals(loadedHistory.getCreatedOn()));
    }

}
