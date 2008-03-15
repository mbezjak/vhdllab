package hr.fer.zemris.vhdllab.api.vhdl;

/**
 * Describes a bit-vector direction in <code>ENTITY</code> block of VHDL code.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public enum VectorDirection {
	/**
	 * First bit of bit-vector is most significant one while last bit is least
	 * significant.
	 */
	DOWNTO,
	/**
	 * First bit of bit-vector is least significant one while last bit is most
	 * significant
	 */
	TO;

	/**
	 * Returns <code>true</code> if this direction is {@link #DOWNTO} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #DOWNTO} of
	 *         <code>false</code> otherwise
	 */
	public boolean isDOWNTO() {
		return this.equals(DOWNTO);
	}

	/**
	 * Returns <code>true</code> if this direction is {@link #TO} of
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this direction is {@link #TO} of
	 *         <code>false</code> otherwise
	 */
	public boolean isTO() {
		return this.equals(TO);
	}

}
