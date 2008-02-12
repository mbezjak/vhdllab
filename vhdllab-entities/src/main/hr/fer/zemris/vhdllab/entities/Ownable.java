package hr.fer.zemris.vhdllab.entities;

/**
 * Any entity that belongs to a user must implement this interface.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
public interface Ownable {

	/**
	 * Maximum user identifier length.
	 */
	public static final int USER_ID_LENGTH = 255;

	/**
	 * Returns a user identifier that this user file belongs to. Return value
	 * will never be <code>null</code>. A user identifier is case
	 * insensitive.
	 * 
	 * @return a user identifier
	 */
	public String getUserId();

}
