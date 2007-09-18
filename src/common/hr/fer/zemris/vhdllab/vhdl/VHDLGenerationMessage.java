package hr.fer.zemris.vhdllab.vhdl;


public class VHDLGenerationMessage extends Message {

	private static final long serialVersionUID = 1L;

	public VHDLGenerationMessage(String entity, String message) {
		this(entity, message, MessageType.ERROR);
	}
	
	public VHDLGenerationMessage(String entity, String message, MessageType type) {
		super(entity, message, type);
	}
	
}
