package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import hr.fer.zemris.vhdllab.dao.impl.support.EntityObjectTable;
import hr.fer.zemris.vhdllab.entities.UserFile;

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

    /*
     * It doesn't matter which entity will be used so we take the simplest one.
     */
    private AbstractEntityDao<EntityObjectTable> dao;

    @PostConstruct
    public void initDao() {
        dao = new AbstractEntityDao<EntityObjectTable>(EntityObjectTable.class) {
        };
        dao.setEntityManagerFactory(entityManagerFactory);
    }

    /**
     * clazz is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor() {
        new AbstractEntityDao<UserFile>(null) {
            // empty implementation
        };
    }

    /**
     * Entity is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void save() {
        dao.save(null);
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
        String query = "select e from EntityObjectTable e";
        // DB is empty here
        assertNull("file not null.", dao.findUniqueResult(query));
    }

    /**
     * Query returns more then one result.
     */
    @Test(expected = IllegalStateException.class)
    public void findUniqueResultMoreThenOne() {
        setupBasicEntities();
        String query = "select e from EntityObjectTable e";
        dao.findUniqueResult(query);
    }

    /**
     * Query returns one unique result.
     */
    @Test
    public void findUniqueResult() {
        setupBasicEntities();
        String query = "select e from EntityObjectTable e where e.name = ?1";
        assertNotNull("file not found.", dao.findUniqueResult(query, NAME));
    }

    private void setupBasicEntities() {
        setupBasicEntity(new EntityObjectTable(NAME));
        setupBasicEntity(new EntityObjectTable(NAME_2));
    }

    private void setupBasicEntity(final EntityObjectTable file) {
        String query = createQuery("EntityObjectTable", "id, version, name",
                "null, 0, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, file.getName().toString());
                return ps.execute();
            }
        });
    }

}
