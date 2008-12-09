package hr.fer.zemris.vhdllab.platform;

import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.support.CommandLineArgumentProcessor;
import hr.fer.zemris.vhdllab.platform.support.GuiInitializer;
import hr.fer.zemris.vhdllab.platform.support.UserLocaleInitializer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Main {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        processCommandLine(args);
        ApplicationContext context = setupDependencyInjectionContainer();
        initializeUserLanguage();
        setupGUI(context);
        initializeWorkspace(context);
    }

    private static void processCommandLine(String[] args) {
        new CommandLineArgumentProcessor().processCommandLine(args);
    }

    private static ApplicationContext setupDependencyInjectionContainer() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        context.registerShutdownHook();
        LOG.debug("Spring container initialized");
        return context;
    }

    private static void initializeUserLanguage() {
        new UserLocaleInitializer().initLocale();
    }

    private static void setupGUI(ApplicationContext context) {
        new GuiInitializer(context).initGUI();
        LOG.debug("GUI initialized");
    }

    private static void initializeWorkspace(ApplicationContext context) {
        WorkspaceInitializer initializer = (WorkspaceInitializer) context
                .getBean("workspaceInitializer");
        initializer.initializeWorkspace();
        LOG.debug("Application initialized");
    }

}
