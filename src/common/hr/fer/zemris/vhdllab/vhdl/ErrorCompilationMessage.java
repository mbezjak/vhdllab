package hr.fer.zemris.vhdllab.vhdl;

public class ErrorCompilationMessage extends CompilationMessage {

	public ErrorCompilationMessage(String message, int row, int column) {
		super(message, row, column);
	}
	
	public static ErrorCompilationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		CompilationMessage msg = CompilationMessage.deserialize(data);
		String message = msg.getMessageText();
		int row = msg.getRow();
		int column = msg.getColumn();
		return new ErrorCompilationMessage(message, row, column);
	}
	
}