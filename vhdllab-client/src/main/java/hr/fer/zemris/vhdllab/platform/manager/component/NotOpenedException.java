package hr.fer.zemris.vhdllab.platform.manager.component;

public class NotOpenedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotOpenedException() {
        super();
    }

    public NotOpenedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotOpenedException(String message) {
        super(message);
    }

    public NotOpenedException(Throwable cause) {
        super(cause);
    }

}
