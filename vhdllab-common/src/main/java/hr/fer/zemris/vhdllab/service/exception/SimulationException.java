package hr.fer.zemris.vhdllab.service.exception;

public class SimulationException extends RuntimeException {

    private static final long serialVersionUID = 7644629772648287958L;

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
