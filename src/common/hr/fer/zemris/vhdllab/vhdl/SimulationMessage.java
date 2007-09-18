package hr.fer.zemris.vhdllab.vhdl;

public final class SimulationMessage extends Message {

	private static final long serialVersionUID = 1L;

	
	public SimulationMessage(String entity, String message) {
		this(entity, message, MessageType.ERROR);
	}
	
	public SimulationMessage(String entity, String message, MessageType type) {
		super(entity, message, type);
	}

	protected SimulationMessage(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
