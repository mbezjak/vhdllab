package hr.fer.zemris.vhdllab.vhdl;

public class SimulationMessage extends Message {

	public SimulationMessage(String message) {
		super(message);
	}
	
	public static SimulationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Message msg = Message.deserialize(data);
		return new SimulationMessage(msg.getMessageText());
	}
}
