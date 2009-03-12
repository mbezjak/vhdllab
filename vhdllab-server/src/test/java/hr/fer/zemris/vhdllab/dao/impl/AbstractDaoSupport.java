package hr.fer.zemris.vhdllab.dao.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jpa.AbstractJpaTests;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Every dao test case should extend this class in order to avoid doing
 * configuration in its own test. It also contains convenience methods like
 * cleaning the database after each test so every test doesn't have to worry
 * about cleaning after itself. This is especially convenient for tests that
 * expect an exception to occur.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/dao-context.xml" })
public abstract class AbstractDaoSupport extends AbstractJpaTests {

    // injecting entity manager factory
    @PersistenceUnit
    @Override
    public void setEntityManagerFactory(
            EntityManagerFactory entityManagerFactory) {
        super.setEntityManagerFactory(entityManagerFactory);
    }

    // injecting transaction manager
    @Autowired
    @Override
    public void setTransactionManager(
            PlatformTransactionManager transactionManager) {
        super.setTransactionManager(transactionManager);
    }

    // injecting data source
    @Autowired
    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    /**
     * Clean database tables after each test.
     */
    @After
    public void cleanupAfterTest() {
        getJdbcTemplate().execute("delete from files");
        getJdbcTemplate().execute("delete from projects");
        getJdbcTemplate().execute("delete from file_history");
        getJdbcTemplate().execute("delete from project_history");
        getJdbcTemplate().execute("delete from client_logs");
    }

    protected String createQuery(String table, String columns, String values) {
        StringBuilder query = new StringBuilder(150);
        query.append("insert into ").append(table);
        query.append(" (").append(columns).append(") values (");
        query.append(values).append(")");
        return query.toString();
    }

}
