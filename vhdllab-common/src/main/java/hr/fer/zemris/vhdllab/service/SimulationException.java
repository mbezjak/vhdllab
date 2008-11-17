package hr.fer.zemris.vhdllab.service;

public class SimulationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SimulationException() {
        super();
    }

    public SimulationException(String message) {
        super(message);
    }

    public SimulationException(Throwable cause) {
        super(cause);
    }

    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }

}
