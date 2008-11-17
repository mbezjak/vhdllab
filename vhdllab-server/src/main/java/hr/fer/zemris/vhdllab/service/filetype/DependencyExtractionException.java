package hr.fer.zemris.vhdllab.service.filetype;

public class DependencyExtractionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DependencyExtractionException() {
        super();
    }

    public DependencyExtractionException(String message) {
        super(message);
    }

    public DependencyExtractionException(Throwable cause) {
        super(cause);
    }

    public DependencyExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

}
