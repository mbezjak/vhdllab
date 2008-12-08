package hr.fer.zemris.vhdllab.service.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * If necessary, initializes libraries when application context is initialized.
 * This listener will help newly installed vhdllab server by setting up
 * libraries (that application depends upon) so user won't have to do it
 * manually by running some sort of sql script. By doing this automatically
 * vhdllab server is easier to install.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class LibraryInitializationListener implements ServletContextListener {

    /**
     * Initializes libraries needed by vhdllab.
     * 
     * @param sce
     *            used to get servet context
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(sce.getServletContext());
        LibraryInitializer groupInitializer = (LibraryInitializer) context
                .getBean("groupLibraryInitializer");
        groupInitializer.initLibrary();
    }

    /**
     * Context destroyed is not used.
     * 
     * @param sce
     *            not used
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
