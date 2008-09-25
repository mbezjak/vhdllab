package hr.fer.zemris.vhdllab.entities;

/**
 * Any entity that belongs to a user must implement this interface.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
interface Ownable {

	/**
	 * Maximum user identifier length.
	 */
	static final int USER_ID_LENGTH = 255;

	/**
	 * Returns a user identifier that this user file belongs to.
	 * 
	 * @return a user identifier
	 */
	Caseless getUserId();

}
