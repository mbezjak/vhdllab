package hr.fer.zemris.vhdllab.server.vhdl;

/**
 * Describes a direction of a port in <code>ENTITY</code> block of VHDL code.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public enum PortDirection {
	/**
	 * An IN direction of a port.
	 */
	IN,
	/**
	 * An OUT direction of a port.
	 */
	OUT,
	/**
	 * An INOUT direction of a port.
	 */
	INOUT,
	/**
	 * A BUFFER direction of a port.
	 */
	BUFFER;

	/**
	 * Returns <code>true</code> if this direction is {@link #IN} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #IN} of
	 *         <code>false</code> otherwise
	 */
	public boolean isIN() {
		return this.equals(IN);
	}

	/**
	 * Returns <code>true</code> if this direction is {@link #OUT} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #OUT} of
	 *         <code>false</code> otherwise
	 */
	public boolean isOUT() {
		return this.equals(OUT);
	}

	/**
	 * Returns <code>true</code> if this direction is {@link #INOUT} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #INOUT} of
	 *         <code>false</code> otherwise
	 */
	public boolean isINOUT() {
		return this.equals(INOUT);
	}

	/**
	 * Returns <code>true</code> if this direction is {@link #BUFFER} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #BUFFER} of
	 *         <code>false</code> otherwise
	 */
	public boolean isBUFFER() {
		return this.equals(BUFFER);
	}

}
