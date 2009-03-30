package hr.fer.zemris.vhdllab.service.exception;

public class NoAvailableProcessException extends RuntimeException {

    private static final long serialVersionUID = -733783747400691581L;

    public NoAvailableProcessException(int processCount) {
        super(createMessage(processCount));
    }

    public NoAvailableProcessException(Throwable cause) {
        super(cause);
    }

    private static String createMessage(int processCount) {
        return "Process count: " + processCount;
    }

}
