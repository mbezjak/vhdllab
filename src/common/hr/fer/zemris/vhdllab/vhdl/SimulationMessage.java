package hr.fer.zemris.vhdllab.vhdl;

public class SimulationMessage extends Message {

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

	public static SimulationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Message msg = Message.deserialize(data);
		//return new SimulationMessage(msg.getMessageText());
		return new SimulationMessage(msg);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
