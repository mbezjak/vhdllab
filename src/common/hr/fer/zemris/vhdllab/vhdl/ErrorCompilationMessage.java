package hr.fer.zemris.vhdllab.vhdl;

public class ErrorCompilationMessage extends CompilationMessage {

	@Deprecated
	public ErrorCompilationMessage(String message, int row, int column) {
		super(message, row, column);
	}
	
	public ErrorCompilationMessage(String entity, String message, int row, int column) {
		super(entity, message, row, column);
	}

	protected ErrorCompilationMessage(Message message, int row, int column) {
		super(message, row, column);
	}

	
	public static ErrorCompilationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		CompilationMessage msg = CompilationMessage.deserialize(data);
		int row = msg.getRow();
		int column = msg.getColumn();
		return new ErrorCompilationMessage(msg, row, column);
	}
	
}