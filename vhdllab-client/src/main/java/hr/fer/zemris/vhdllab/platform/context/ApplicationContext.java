package hr.fer.zemris.vhdllab.platform.context;

import hr.fer.zemris.vhdllab.entities.Caseless;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

public final class ApplicationContext {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(ApplicationContext.class);

    private Caseless userId;
    private Environment environment;
    private JFrame frame;
    private boolean applicationInitialized = false;
    private ApplicationState state = ApplicationState.RUNNING;

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

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setApplicationInitialized(boolean applicationInitialized) {
        this.applicationInitialized = applicationInitialized;
    }

    public boolean isApplicationInitialized() {
        return applicationInitialized;
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
        LOG.debug("Application state set to " + this.state);
    }

    public boolean isRunning() {
        return state.equals(ApplicationState.RUNNING);
    }

}
