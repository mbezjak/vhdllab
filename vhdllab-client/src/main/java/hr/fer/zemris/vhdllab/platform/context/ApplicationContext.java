package hr.fer.zemris.vhdllab.platform.context;

import hr.fer.zemris.vhdllab.entities.Caseless;

import org.apache.log4j.Logger;

public final class ApplicationContext {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(ApplicationContext.class);

    private Caseless userId;
    private Environment environment;

    public Caseless getUserId() {
        return userId;
    }

    public void setUserId(Caseless userId) {
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
