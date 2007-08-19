package hr.fer.zemris.vhdllab.client.core.log;

/**
 * Represents a type of a message.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public enum MessageType {

	/**
	 * A message type that indicates an error in a application. Either an actual
	 * bug in application or for example that a server is not responding.
	 */
	ERROR,
	/**
	 * A message type that indicates a successful action. For example: a
	 * resource has been saved successfully.
	 */
	SUCCESSFUL,
	/**
	 * A message type that indicates an information. For example: an editor has
	 * been closed.
	 */
	INFORMATION;

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