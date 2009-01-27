package hr.fer.zemris.vhdllab.platform;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.log.StdErrConsumer;
import hr.fer.zemris.vhdllab.platform.log.StdOutConsumer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.support.CommandLineArgumentProcessor;
import hr.fer.zemris.vhdllab.platform.support.GuiInitializer;
import hr.fer.zemris.vhdllab.platform.support.UserLocaleInitializer;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Main {

    /**
     * Logger for this class
     */
    private static Logger logger;

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*
                 * Application is initialized in EDT because of swing components
                 * used throughout the platform.
                 */
                try {
                    initializeApplication(args);
                } catch (Exception e) {
                    /*
                     * Application is either fully instantiated or not at all!
                     */
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

    static void initializeApplication(String[] args) {
        long start = System.currentTimeMillis();
        initializeLogging();
        processCommandLine(args);
        ApplicationContext context = setupDependencyInjectionContainer();
        initializeUserLanguage();
        setupGUI(context);
        initializeWorkspace(context);
        ApplicationContextHolder.getContext().setApplicationInitialized(true);
        long end = System.currentTimeMillis();
        logger.debug("Application finished initialization in " + (end - start)
                + "ms");
    }

    private static void initializeLogging() {
        StdOutConsumer.instance().substituteStream();
        StdErrConsumer.instance().substituteStream();
        /*
         * Its important to initialize log4j after standard streams are replaced
         * because log4j doesn't directly invoke methods from System.out (or
         * System.err). Instead it wraps them around QuietWriter. Because we
         * want ConsoleAppender to act exactly the way System.out does, log4j
         * needs to be initialized after substituting standard streams!
         */
        logger = Logger.getLogger(Main.class);
    }

    private static void processCommandLine(String[] args) {
        new CommandLineArgumentProcessor().processCommandLine(args);
    }

    private static ApplicationContext setupDependencyInjectionContainer() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        context.registerShutdownHook();
        logger.debug("Spring container initialized");
        return context;
    }

    private static void initializeUserLanguage() {
        new UserLocaleInitializer().initLocale();
    }

    private static void setupGUI(ApplicationContext context) {
        GuiInitializer initializer = (GuiInitializer) context
                .getBean("guiInitializer");
        initializer.initGUI();
    }

    private static void initializeWorkspace(ApplicationContext context) {
        WorkspaceInitializer initializer = (WorkspaceInitializer) context
                .getBean("workspaceInitializer");
        initializer.initWorkspace();
    }

}
