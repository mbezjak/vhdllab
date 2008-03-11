package hr.fer.zemris.vhdllab.server.results;

/**
 * Represents type of a result message.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
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

	/**
	 * Returns <code>true</code> if this type is {@link #SUCCESSFUL} or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this type is {@link #SUCCESSFUL} or
	 *         <code>false</code> otherwise
	 */
	public boolean isSuccessful() {
		return this.equals(SUCCESSFUL);
	}

	/**
	 * Returns <code>true</code> if this type is {@link #ERROR} or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this type is {@link #ERROR} or
	 *         <code>false</code> otherwise
	 */
	public boolean isError() {
		return this.equals(ERROR);
	}

	/**
	 * Returns <code>true</code> if this type is {@link #INFORMATION} or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this type is {@link #INFORMATION} or
	 *         <code>false</code> otherwise
	 */
	public boolean isInformation() {
		return this.equals(INFORMATION);
	}

}
