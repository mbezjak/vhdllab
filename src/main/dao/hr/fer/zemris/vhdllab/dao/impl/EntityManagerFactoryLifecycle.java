package hr.fer.zemris.vhdllab.dao.impl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A servlet context listener for creating and destroying entity manager factory
 * associated with servlet context. An entity manager factory will be created
 * when web application is initializing and will be destroyed when web
 * application is destroying.
 * 
 * @author Miro Bezjak
 */
public class EntityManagerFactoryLifecycle implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		EntityManagerUtil.createEntityManagerFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		EntityManagerUtil.shutdown();
	}

}
