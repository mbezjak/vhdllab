package hr.fer.zemris.vhdllab.platform.manager.workspace;

public class FileAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileAlreadyExistsException() {
        super();
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }

    public FileAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public FileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
