package hr.fer.zemris.vhdllab.vhdl;

public class WarningCompilationMessage extends CompilationMessage {

	private static final long serialVersionUID = 1L;

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
	
}