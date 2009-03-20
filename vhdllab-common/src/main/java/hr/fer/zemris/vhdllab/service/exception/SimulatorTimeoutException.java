package hr.fer.zemris.vhdllab.service.exception;

public class SimulatorTimeoutException extends RuntimeException {

    private static final long serialVersionUID = -5200477103000002244L;

    public SimulatorTimeoutException() {
        super();
    }

    public SimulatorTimeoutException(String message) {
        super(message);
    }

    public SimulatorTimeoutException(Throwable cause) {
        super(cause);
    }

    public SimulatorTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
