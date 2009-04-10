package hr.fer.zemris.vhdllab.platform;

import hr.fer.zemris.vhdllab.platform.log.StdErrConsumer;
import hr.fer.zemris.vhdllab.platform.log.StdOutConsumer;
import hr.fer.zemris.vhdllab.platform.support.CommandLineArgumentProcessor;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationLauncher;

public final class Main {

    /**
     * Logger for this class
     */
    private static Logger logger;

    public static void main(String[] args) {
        initializeApplication(args);
    }

    private static void initializeApplication(String[] args) {
        long start = System.currentTimeMillis();
        initializeLogging();
        processCommandLine(args);
        /*
         * Only after command line is processed should logging be used!
         */
        continueStartupInRichClientApplicationLoader();
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
         * needs to be initialized after substituting standard streams (in order
         * to wrap correct stdout and stderr stream)!
         */
        logger = Logger.getLogger(Main.class);
    }

    private static void processCommandLine(String[] args) {
        new CommandLineArgumentProcessor().processCommandLine(args);
    }

    private static void continueStartupInRichClientApplicationLoader() {
        String rootContextDirectoryClassPath = "/ctx";

        String startupContextPath = rootContextDirectoryClassPath
                + "/richclient-startup-context.xml";

        String richclientApplicationContextPath = rootContextDirectoryClassPath
                + "/richclient-application-context.xml";

        String viewsApplicationContextPath = rootContextDirectoryClassPath
                + "/richclient-views-context.xml";
        String remotingApplicationContextPath = "/applicationContext.xml";

        new ApplicationLauncher(startupContextPath, new String[] {
                richclientApplicationContextPath, viewsApplicationContextPath,
                remotingApplicationContextPath });
    }

}
