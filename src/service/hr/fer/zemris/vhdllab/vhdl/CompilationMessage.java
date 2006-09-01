package hr.fer.zemris.vhdllab.vhdl;

public class CompilationMessage extends Message {

	private int row;
	private int column;
	
	public CompilationMessage(String message, int row, int column) {
		super(message);
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}
