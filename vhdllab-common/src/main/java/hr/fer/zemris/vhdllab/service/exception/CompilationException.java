package hr.fer.zemris.vhdllab.service.exception;

public class CompilationException extends RuntimeException {

    private static final long serialVersionUID = 2557355857702130080L;

    public CompilationException() {
        super();
    }

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(Throwable cause) {
        super(cause);
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
    }

}
