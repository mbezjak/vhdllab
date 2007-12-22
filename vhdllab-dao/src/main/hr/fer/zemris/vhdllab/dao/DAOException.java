package hr.fer.zemris.vhdllab.dao;

/**
 * Exception indicating an exceptional condition in DAO layer.
 * 
 * @author Miro Bezjak
 */
public class DAOException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DAO exception without a message and a cause.
	 */
	public DAOException() {
		super();
	}

	/**
	 * Creates a DAO exception with a specified <code>message</code>.
	 * 
	 * @param message
	 *            a specified message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Creates a DAO exception with a specified <code>message</code> and
	 * <code>cause</code>.
	 * 
	 * @param message
	 *            a specified message
	 * @param cause
	 *            a cause of this exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a DAO exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param cause
	 *            a cause of this exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

}
