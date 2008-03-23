package hr.fer.zemris.vhdllab.dao;

import hr.fer.zemris.vhdllab.server.ServerException;

/**
 * Exception indicating an exceptional condition in DAO tier.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public class DAOException extends ServerException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a DAO exception with a specified <code>message</code>.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param message
	 *            a specified message
	 */
	public DAOException(short statusCode, String message) {
		super(statusCode, message);
	}
	
	/**
	 * Creates a DAO exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param cause
	 *            a cause of this exception
	 */
	public DAOException(ServerException cause) {
		this(cause.getStatusCode(), null, cause);
	}
	
	/**
	 * Creates a DAO exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param cause
	 *            a cause of this exception
	 */
	public DAOException(short statusCode, Throwable cause) {
		super(statusCode, cause);
	}

	/**
	 * Creates a DAO exception with a specified <code>message</code> and
	 * <code>cause</code>.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param message
	 *            a specified message
	 * @param cause
	 *            a cause of this exception
	 * @throws NullPointerException
	 *             if both <code>message</code> and <code>cause</code> is
	 *             <code>null</code>
	 */
	public DAOException(short statusCode, String message, Throwable cause) {
		super(statusCode, message, cause);
	}

}
