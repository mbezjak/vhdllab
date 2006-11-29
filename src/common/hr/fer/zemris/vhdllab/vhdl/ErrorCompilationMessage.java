package hr.fer.zemris.vhdllab.vhdl;

public class ErrorCompilationMessage extends CompilationMessage {

	public ErrorCompilationMessage(String message, int row, int column) {
		super(message, row, column);
	}
}
