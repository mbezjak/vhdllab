package hr.fer.zemris.vhdllab.platform.context;

import org.apache.log4j.Logger;

public final class ApplicationContext {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(ApplicationContext.class);

    private String userId;
    private Environment environment;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
        LOG.debug("Environment set to " + this.environment);
    }

    public boolean isDevelopment() {
        return getEnvironment().equals(Environment.DEVELOPMENT);
    }

}
