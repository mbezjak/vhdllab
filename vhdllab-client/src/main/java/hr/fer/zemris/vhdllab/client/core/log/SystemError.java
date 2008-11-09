package hr.fer.zemris.vhdllab.client.core.log;


/**
 * Represents an error message of the system. This class is immutable and
 * therefor thread-safe.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public final class SystemError extends SystemMessage {

    /**
     * The cause of an error.
     */
    private Throwable cause;

    /**
     * Constructs a message with current time and <code>ERROR</code> message
     * type.
     * 
     * @param cause
     *            an exception that occurred
     * @throws NullPointerException
     *             if <code>exception</code> is <code>null</code>
     */
    public SystemError(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    /**
     * Returns a cause of an error.
     * 
     * @return a cause of an error
     */
    public Throwable getCause() {
        return cause;
    }

}
