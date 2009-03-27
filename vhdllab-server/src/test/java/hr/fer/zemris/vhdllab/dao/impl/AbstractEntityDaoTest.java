package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.NamedEntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.NamedEntityTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

/**
 * Tests for {@link AbstractEntityDao} class.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class AbstractEntityDaoTest extends AbstractDaoSupport {

    private AbstractEntityDao<?> dao;

    @PostConstruct
    public void initDao() {
        /*
         * It doesn't matter which entity will be used so we take the simplest
         * one but also one that will be useful in find* tests.
         */
        dao = new NamedEntityDao();
        dao.setEntityManagerFactory(entityManagerFactory);
    }

    /**
     * clazz is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor() {
        new AbstractEntityDao<NamedEntityTable>(null) {
            // empty implementation
        };
    }

    /**
     * Entity is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void save() {
        dao.persist(null);
    }

    /**
     * Entity is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void merge() {
        dao.merge(null);
    }

    /**
     * id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void load() {
        dao.load(null);
    }

    /**
     * non-existing id
     */
    @Test
    public void load2() {
        assertNull("file not null.", dao.load(Integer.MAX_VALUE));
    }

    /**
     * entity is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void delete() {
        dao.delete(null);
    }

    /**
     * Query returns no result.
     */
    @Test
    public void findUniqueResultEmpty() {
        String query = "select e from NamedEntityTable e";
        // DB is empty here
        assertNull("file not null.", dao.findUniqueResult(query));
    }

    /**
     * Query returns more then one result.
     */
    @Test(expected = IllegalStateException.class)
    public void findUniqueResultMoreThenOne() {
        setupNamedEntities();
        String query = "select e from NamedEntityTable e";
        dao.findUniqueResult(query);
    }

    /**
     * Query returns one unique result.
     */
    @Test
    public void findUniqueResult() {
        setupNamedEntities();
        String query = "select e from NamedEntityTable e where e.name = ?1";
        assertNotNull("file not found.", dao.findUniqueResult(query, "name"));
    }

    private void setupNamedEntities() {
        setupNamedEntity("name");
        setupNamedEntity("name2");
    }

    private void setupNamedEntity(final String name) {
        String query = createInsertStatement("NamedEntityTable",
                "id, version, name", "null, 0, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, name);
                return ps.execute();
            }
        });
    }

}