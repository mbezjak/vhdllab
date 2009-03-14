package hr.fer.zemris.vhdllab.service.exception;

public class CircuitInterfaceExtractionException extends RuntimeException {

    private static final long serialVersionUID = 6206862371948158173L;

    public CircuitInterfaceExtractionException() {
        super();
    }

    public CircuitInterfaceExtractionException(String message) {
        super(message);
    }

    public CircuitInterfaceExtractionException(Throwable cause) {
        super(cause);
    }

    public CircuitInterfaceExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

}
