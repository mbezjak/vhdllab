package hr.fer.zemris.vhdllab.dao.impl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A servlet context listener for registering and unregistering persistence JMX.
 * 
 * @author Miro Bezjak
 */
public class PersistenceJMXLifecycle implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			EntityManagerUtil.registerPersistenceJMX();
		} catch (Exception e) {
			// already logged
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			EntityManagerUtil.unregisterPersistenceJMX();
		} catch (Exception e) {
			// already logged
		}
	}

}
