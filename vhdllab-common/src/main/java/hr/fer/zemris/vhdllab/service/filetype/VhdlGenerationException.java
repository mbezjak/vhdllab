package hr.fer.zemris.vhdllab.service.filetype;

public class VhdlGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

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
