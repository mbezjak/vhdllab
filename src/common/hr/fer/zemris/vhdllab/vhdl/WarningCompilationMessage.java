package hr.fer.zemris.vhdllab.vhdl;

public class WarningCompilationMessage extends CompilationMessage {

	@Deprecated
	public WarningCompilationMessage(String message, int row, int column) {
		super(message, row, column);
	}
	
	public WarningCompilationMessage(String entity, String message, int row, int column) {
		super(entity, message, row, column);
	}

	protected WarningCompilationMessage(Message message, int row, int column) {
		super(message, row, column);
	}

	public static WarningCompilationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		CompilationMessage msg = CompilationMessage.deserialize(data);
		int row = msg.getRow();
		int column = msg.getColumn();
		return new WarningCompilationMessage(msg, row, column);
	}
	
}