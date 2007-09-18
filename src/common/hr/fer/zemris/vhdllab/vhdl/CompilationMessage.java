package hr.fer.zemris.vhdllab.vhdl;


public final class CompilationMessage extends Message {
	
	private static final long serialVersionUID = 1L;

	private int row;
	private int column;
	
	protected CompilationMessage(Message message, int row, int column) {
		super(message);
		if(row < 0 || column < 0) throw new IllegalArgumentException("Row or column can not be negative.");
		this.row = row;
		this.column = column;
	}

	public CompilationMessage(String entity, String message, int row, int column) {
		this(entity, message, MessageType.ERROR, row, column);
	}
	
	public CompilationMessage(String entity, String message, MessageType type, int row, int column) {
		super(entity, message, type);
		if(row < 0 || column < 0) throw new IllegalArgumentException("Row or column can not be negative.");
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CompilationMessage))
			return false;
		final CompilationMessage other = (CompilationMessage) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString()+" @("+row+","+column+")";
	}
}