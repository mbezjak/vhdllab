package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

/**
 * Represents a type of a message.
 * 
 * @author Miro Bezjak
 */
public enum MessageType {

	ERROR, SUCCESSFUL, INFORMATION;

	private MessageType() {
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is
	 * <code>successful</code>.
	 * 
	 * @return<code>true</code> if this MessageEnum is
	 *         <code>successful</code>; <code>false</code> otherwise
	 */
	public boolean isSuccessful() {
		return this.equals(SUCCESSFUL);
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is <code>error</code>.
	 * 
	 * @return<code>true</code> if this MessageEnum is <code>error</code>;
	 *         <code>false</code> otherwise
	 */
	public boolean isError() {
		return this.equals(ERROR);
	}

	/**
	 * Returns <code>true</code> if this MessageEnum is
	 * <code>information</code>.
	 * 
	 * @return<code>true</code> if this MessageEnum is
	 *         <code>information</code>; <code>false</code> otherwise
	 */
	public boolean isInformation() {
		return this.equals(INFORMATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.name();
	}
}