package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.initialize.InitServerData;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializes a servlet.
 */
public class InitManagerProvider implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent ev) {
		ev.getServletContext().log("InitManagerProvider listener started.");
		
		// initialize server data
		InitServerData initData = new InitServerData(ServiceContainer.instance());
		try {
            initData.initFiles();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent ev) {
		//InitServerData.cleanServerData();
		ev.getServletContext().log("InitManagerProvider listener finished.");
	}

}
