package hr.fer.zemris.vhdllab.service.exception;

public class SimulatorTimeoutException extends RuntimeException {

    private static final long serialVersionUID = -733783747400691581L;

    public SimulatorTimeoutException(int timeout) {
        super(createMessage(timeout));
    }

    private static String createMessage(int timeout) {
        return "Timeout: " + timeout;
    }

}
