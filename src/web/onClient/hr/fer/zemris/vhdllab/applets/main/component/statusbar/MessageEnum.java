package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

/**
 * Represents a type of a message.
 * 
 * @author Miro Bezjak
 */
public enum MessageEnum {
	
	Error, Successfull, Information;
	
	private MessageEnum() {}
	
	/**
	 * Returns <code>true</code> if this MessageEnum is <code>successfull</code>.
	 * @return<code>true</code> if this MessageEnum is <code>successfull</code>;
	 * 		<code>false</code> otherwise
	 */
	public boolean isSuccessful() {
		return this.equals(Successfull);
	}
	
	/**
	 * Returns <code>true</code> if this MessageEnum is <code>error</code>.
	 * @return<code>true</code> if this MessageEnum is <code>error</code>;
	 * 		<code>false</code> otherwise
	 */
	public boolean isError() {
		return this.equals(Error);
	}
	
	/**
	 * Returns <code>true</code> if this MessageEnum is <code>information</code>.
	 * @return<code>true</code> if this MessageEnum is <code>information</code>;
	 * 		<code>false</code> otherwise
	 */
	public boolean isInformation() {
		return this.equals(Information);
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.name();
	}
}