package hr.fer.zemris.vhdllab.dao.impl.support;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jpa.AbstractJpaTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    protected EntityManager entityManager;

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

    @Before
    public void initEntityManager() {
        createEntityManager();
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
        getJdbcTemplate().execute("delete from BaseEntityTable");
        getJdbcTemplate().execute("delete from NamedEntityTable");
        getJdbcTemplate().execute("delete from FileInfoTable");
        getJdbcTemplate().execute("delete from ProjectInfoTable");
        getJdbcTemplate().execute("delete from HistoryTable");
        closeEntityManager();
    }

    protected void createEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(entityManagerFactory,
                new EntityManagerHolder(entityManager));
    }

    protected void closeEntityManager() {
        if(entityManager != null) {
            entityManager.close();
            TransactionSynchronizationManager.unbindResource(entityManagerFactory);
            entityManager = null;
        }
    }

    protected String createInsertStatement(String table, String columns,
            String values) {
        StringBuilder statement = new StringBuilder(150);
        statement.append("insert into ").append(table);
        statement.append(" (").append(columns).append(") values (");
        statement.append(values).append(")");
        return statement.toString();
    }

}
