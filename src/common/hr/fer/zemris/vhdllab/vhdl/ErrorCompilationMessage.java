package hr.fer.zemris.vhdllab.vhdl;

public class ErrorCompilationMessage extends CompilationMessage {

	private static final long serialVersionUID = 1L;


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
	
}