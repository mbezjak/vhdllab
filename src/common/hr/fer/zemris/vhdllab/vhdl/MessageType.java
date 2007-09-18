package hr.fer.zemris.vhdllab.vhdl;

/**
 * Represents a type of a message (of a result).
 * 
 * @author Miro Bezjak
 */
public enum MessageType {

	/**
	 * A message type that indicates a successful action.
	 */
	SUCCESSFUL,
	/**
	 * A message type that indicates an information.
	 */
	INFORMATION,
	/**
	 * A message type that indicates an error.
	 */
	ERROR,
	/**
	 * A message type that indicates a warning.
	 */
	WARNING;

	private MessageType() {
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is
	 * <code>successful</code>.
	 * 
	 * @return <code>true</code> if this MessageEnum is
	 *         <code>successful</code>; <code>false</code> otherwise
	 */
	public boolean isSuccessful() {
		return this.equals(SUCCESSFUL);
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is <code>error</code>.
	 * 
	 * @return <code>true</code> if this MessageEnum is <code>error</code>;
	 *         <code>false</code> otherwise
	 */
	public boolean isError() {
		return this.equals(ERROR);
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is
	 * <code>information</code>.
	 * 
	 * @return <code>true</code> if this MessageEnum is
	 *         <code>information</code>; <code>false</code> otherwise
	 */
	public boolean isInformation() {
		return this.equals(INFORMATION);
	}

}