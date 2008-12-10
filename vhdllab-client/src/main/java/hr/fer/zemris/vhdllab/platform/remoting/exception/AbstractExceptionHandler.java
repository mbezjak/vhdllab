package hr.fer.zemris.vhdllab.platform.remoting.exception;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContext;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.context.ApplicationState;

public abstract class AbstractExceptionHandler implements ExceptionHandler {

    /**
     * An exit status error indicating abnormal application termination.
     */
    private static final int EXIT_STATUS_ERROR = 1;

    protected boolean exitIfNecessary() {
        ApplicationContext context = ApplicationContextHolder.getContext();
        if (!context.isApplicationInitialized()) {
            /*
             * Can forcefully exit because application has not yet been fully
             * initialized (so shutdownManager can't be called).
             */
            System.exit(EXIT_STATUS_ERROR);
            // no need for "return false;"
        }
        context.setState(ApplicationState.ERRONEOUS);
        return true;
    }

    protected boolean isOfType(Throwable t, Class<? extends Exception> typeClass) {
        if (t != null) {
            return t.getClass() == typeClass
                    || isOfType(t.getCause(), typeClass);
        }
        return false;
    }

}
