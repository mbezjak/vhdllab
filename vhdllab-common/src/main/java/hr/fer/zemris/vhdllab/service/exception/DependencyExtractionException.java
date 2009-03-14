package hr.fer.zemris.vhdllab.service.exception;

public class DependencyExtractionException extends RuntimeException {

    private static final long serialVersionUID = 826672293510578867L;

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
