package hr.fer.zemris.vhdllab.server.results;

/**
 * Represents a compilation message.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class CompilationMessage extends Message {

	private static final long serialVersionUID = 1L;

	/**
	 * A row where message occurred.
	 * 
	 * @serial
	 */
	private int row;
	/**
	 * A column where message occurred.
	 * 
	 * @serial
	 */
	private int column;

	/**
	 * Copy constructor.
	 * 
	 * @param message
	 *            a message to copy
	 * @param row
	 *            a row where message occurred
	 * @param column
	 *            a column where message occurred
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>(row < 0 || column < 0)</code>
	 */
	protected CompilationMessage(Message message, int row, int column) {
		super(message);
		setRowAndColumn(row, column);
	}

	/**
	 * Creates a compilation message out of specified parameters. Entity name
	 * will be set to <code>null</code>.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @param row
	 *            a row where message occurred
	 * @param column
	 *            a column where message occurred
	 * @throws NullPointerException
	 *             if <code>type</code> or <code>messageText</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>(row < 0 || column < 0)</code>
	 */
	public CompilationMessage(MessageType type, String messageText, int row,
			int column) {
		this(type, messageText, null, row, column);
	}

	/**
	 * Creates a compilation message out of specified parameters.
	 * 
	 * @param type
	 *            a type of a message
	 * @param messageText
	 *            a text of a message
	 * @param entityName
	 *            an entity name of a unit for which message is generated.
	 *            Please note that this can be an empty string, if appropriate
	 *            information could not be determined
	 * @param row
	 *            a row where message occurred
	 * @param column
	 *            a column where message occurred
	 * @throws NullPointerException
	 *             if <code>type</code> or <code>messageText</code> is
	 *             <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>(row < 0 || column < 0)</code>
	 */
	public CompilationMessage(MessageType type, String messageText,
			String entityName, int row, int column) {
		super(type, messageText, entityName);
		setRowAndColumn(row, column);
	}

	/**
	 * Sets a row and column.
	 * 
	 * @param row
	 *            a row where message occurred
	 * @param column
	 *            a column where message occurred
	 * @throws IllegalArgumentException
	 *             if <code>(row < 0 || column < 0)</code>
	 */
	private void setRowAndColumn(int row, int column) {
		if (row < 0) {
			throw new IllegalArgumentException("Row cant be negative, was: "
					+ row);
		}
		if (column < 0) {
			throw new IllegalArgumentException("Column cant be negative, was: "
					+ column);
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns a row where message occurred.
	 * 
	 * @return a row where message occurred
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns a column where message occurred.
	 * 
	 * @return a column where message occurred
	 */
	public int getColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.server.results.Message#toString()
	 */
	@Override
	public String toString() {
		return "[" + row + "," + column + "]" + super.toString();
	}

	/**
	 * Make a defensive copy.
	 */
	@Override
	protected Object readResolve() {
		return new CompilationMessage(this, row, column);
	}

}
