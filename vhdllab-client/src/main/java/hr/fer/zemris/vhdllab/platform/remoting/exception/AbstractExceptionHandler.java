package hr.fer.zemris.vhdllab.platform.remoting.exception;

public abstract class AbstractExceptionHandler implements ExceptionHandler {

    /**
     * An exit status error indicating abnormal application termination.
     */
    private static final int EXIT_STATUS_ERROR = 1;

    protected void exitWithError() {
        /*
         * Can forcefully exit because application has not yet been fully
         * initialized (so shutdownManager can't be called).
         */
        System.exit(EXIT_STATUS_ERROR);
    }

    protected boolean isOfType(Throwable t, Class<? extends Exception> typeClass) {
        return t != null && t.getClass() == typeClass;
    }

}
