package hr.fer.zemris.vhdllab.vhdl;

public class SimulationMessage extends Message {

	private static final long serialVersionUID = 1L;

	/**
	 * @param entity Entity name of simulation unit for which message is generated 
	 * @param message message
	 */
	public SimulationMessage(String entity, String message) {
		super(entity, message);
	}
	
	@Deprecated
	public SimulationMessage(String message) {
		super(message);
	}
	
	protected SimulationMessage(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
