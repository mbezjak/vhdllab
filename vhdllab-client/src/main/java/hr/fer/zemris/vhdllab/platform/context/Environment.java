package hr.fer.zemris.vhdllab.platform.context;

import org.apache.log4j.Logger;

public enum Environment {
    DEVELOPMENT, PRODUCTION;

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(Environment.class);

    private static Environment currentEnvironment;

    public static Environment getCurrentEnvironment() {
        if (currentEnvironment == null) {
            setCurrentEnvironment(DEVELOPMENT);
        }
        return currentEnvironment;
    }

    public static void setCurrentEnvironment(Environment env) {
        currentEnvironment = env;
        LOG.debug("Environment set to " + currentEnvironment);
    }

    public static boolean isDevelopment() {
        return getCurrentEnvironment().equals(DEVELOPMENT);
    }

}
