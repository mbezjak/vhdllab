package hr.fer.zemris.vhdllab.platform.manager.workspace;

public class ProjectAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProjectAlreadyExistsException() {
        super();
    }

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }

    public ProjectAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ProjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
