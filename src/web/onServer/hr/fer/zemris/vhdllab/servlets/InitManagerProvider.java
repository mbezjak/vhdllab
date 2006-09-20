package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializes a servlet.
 */
public class InitManagerProvider implements ServletContextListener {

	public void contextInitialized(ServletContextEvent ev) {
		ev.getServletContext().log("InitManagerProvider listener started.");
		// create new manager provider
		ManagerProvider prov = new SampleManagerProvider();
		// store it in application context
		ev.getServletContext().setAttribute("managerProvider",prov);
	}

	public void contextDestroyed(ServletContextEvent ev) {
		ev.getServletContext().removeAttribute("managerProvider");
		ev.getServletContext().log("InitManagerProvider listener finished.");
	}

}
