package hr.fer.zemris.vhdllab.service.exception;

public class VhdlGenerationException extends RuntimeException {

    private static final long serialVersionUID = 7645646979943880022L;

    public VhdlGenerationException() {
        super();
    }

    public VhdlGenerationException(String message) {
        super(message);
    }

    public VhdlGenerationException(Throwable cause) {
        super(cause);
    }

    public VhdlGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

}
