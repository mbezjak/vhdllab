package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jmx.StatisticsService;

/**
 * This is a help class for creating entity managers.
 */
public class EntityManagerUtil {

	/**
	 * A name of persistence unit in persistence.xml file.
	 */
	private static final String PERSISTENCE_UNIT = "hibernate";

	/**
	 * A logger for this class.
	 */
	private static final Logger log = Logger.getLogger(EntityManagerUtil.class);

	/**
	 * A persistence provider object name for JMX.
	 */
	private static ObjectName PERSISTENCE_OBJECT_NAME;

	/*
	 * Persistence managers.
	 */
	private static final EntityManagerFactory factory;
	private static final ThreadLocal<EntityManager> entityManager = new ThreadLocal<EntityManager>();

	static {
		// initialize factory
		try {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		} catch (RuntimeException e) {
			log.fatal("Error in building entity manager factory", e);
			throw new ExceptionInInitializerError(e);
		}

		// initialize persistence object name
		try {
			PERSISTENCE_OBJECT_NAME = new ObjectName(
					"Hibernate:type=statistics,application=VHDLLab");
		} catch (Exception e) {
			log.error("Error during instantiating persistence object name", e);
			// suppress error
		}
	}
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private EntityManagerUtil() {
	}

	/**
	 * Creates an entity manager factor. Repeated invocation of this method will
	 * do nothing (i.e. an entity manager factory can't initialize twice).
	 */
	public static void createEntityManagerFactory() {
		/*
		 * Method is empty because initialization is done in a static block
		 * instead to ensure a final modifier.
		 */
	}

	/**
	 * Closes an entity manager factory.
	 */
	public static void shutdown() {
		try {
			factory.close();
		} catch (RuntimeException e) {
			log.error("Error during closing of entity manager factory", e);
		}
	}

	/**
	 * Returns an entity manager for current thread. If no entity manager is
	 * active for current thread then this method will first create new entity
	 * manager then return it.
	 * 
	 * @return an entity manager for current thread
	 */
	public static EntityManager currentEntityManager() {
		EntityManager em = entityManager.get();
		// Create a new entity manager, if this Thread has none yet
		if (em == null) {
			try {
				em = factory.createEntityManager();
			} catch (RuntimeException e) {
				log.error("Error during creating entity manager", e);
				// rethrow exception
				throw e;
			}
			entityManager.set(em);
		}
		return em;
	}

	/**
	 * Closes an entity manager for current thread.
	 */
	public static void closeEntityManager() {
		EntityManager em = entityManager.get();
		entityManager.set(null);
		if (em != null) {
			try {
				em.close();
			} catch (RuntimeException e) {
				log.error("Error during closing entity manager", e);
			}
		}
	}

	/**
	 * Creates and returns an entity transaction object.
	 * 
	 * @return an entity transaction object
	 */
	private static EntityTransaction getTransaction() {
		return currentEntityManager().getTransaction();
	}

	/**
	 * Begins a transaction.
	 * 
	 * @throws DAOException
	 *             if exception occurred during beginning of transaction
	 */
	public static void beginTransaction() throws DAOException {
		EntityTransaction tx = getTransaction();
		if (!tx.isActive()) {
			try {
				tx.begin();
			} catch (PersistenceException e) {
				log.error("Error during beginning transaction", e);
				throw new DAOException(e);
			}
		}
	}

	/**
	 * Commits a transaction. If no transaction is opened then this method will
	 * do nothing.
	 * 
	 * @throws DAOException
	 *             if exception occurred during committing of transaction
	 */
	public static void commitTransaction() throws DAOException {
		EntityTransaction tx = getTransaction();
		if (tx.isActive()) {
			try {
				tx.commit();
			} catch (PersistenceException e) {
				log.warn("Possible error during commiting transaction", e);
				throw new DAOException(e);
			}
		}
	}

	/**
	 * Rollbacks a transaction.
	 */
	public static void rollbackTransaction() {
		EntityTransaction tx = getTransaction();
		if (tx.isActive()) {
			try {
				tx.rollback();
			} catch (PersistenceException e) {
				log.error("Error during transaction rollback", e);
			}
		}
	}

	/**
	 * Registers an MBean service (usually some sort of statistics) to platform
	 * MBean Server. This method is persistence provider dependent.
	 * 
	 * @throws Exception
	 *             if any error occurs
	 */
	public static void registerPersistenceJMX() throws Exception {
		if(PERSISTENCE_OBJECT_NAME == null) {
			throw new NullPointerException("Persistence object name not initialized");
		}
		try {
			Session session = (Session) currentEntityManager().getDelegate();
			SessionFactory sf = session.getSessionFactory();
			StatisticsService statsMBean = new StatisticsService();
			statsMBean.setSessionFactory(sf);
			statsMBean.setStatisticsEnabled(true);
			
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(statsMBean, PERSISTENCE_OBJECT_NAME);
		} catch (Exception e) {
			/*
			 * Log and rethrow exception
			 */
			log.error("Error during registring hibernate statistics service to MBean server", e);
			throw e;
		}
	}

	/**
	 * Unregisters an MBean service.
	 * 
	 * @throws Exception
	 *             if any error occurs
	 */
	public static void unregisterPersistenceJMX() throws Exception {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.unregisterMBean(PERSISTENCE_OBJECT_NAME);
		} catch (Exception e) {
			/*
			 * Log and rethrow exception
			 */
			log.error("Error during unregistring hibernate statistics service", e);
			throw e;
		}
	}

}