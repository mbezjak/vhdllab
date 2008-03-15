package hr.fer.zemris.vhdllab.server;

import hr.fer.zemris.vhdllab.api.StatusCodes;

/**
 * Exception indicating an exceptional condition in server. This exception also
 * contains information regarding why it occurred.
 * 
 * @see StatusCodes
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * A code indicating why this exception occurred.
	 */
	private short statusCode;

	/**
	 * Creates a server exception with a specified <code>message</code>.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param message
	 *            a specified message
	 */
	public ServerException(short statusCode, String message) {
		this(statusCode, message, null);
	}

	/**
	 * Creates a server exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param cause
	 *            a cause of this exception
	 */
	public ServerException(ServerException cause) {
		this(cause.getStatusCode(), null, cause);
	}
	
	/**
	 * Creates a server exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param cause
	 *            a cause of this exception
	 */
	public ServerException(short statusCode, Throwable cause) {
		this(statusCode, null, cause);
	}

	/**
	 * Creates a server exception with a specified <code>message</code> and
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
	public ServerException(short statusCode, String message, Throwable cause) {
		super(message, cause);
		if (message == null && cause == null) {
			throw new NullPointerException(
					"Either message or cause must be set");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Returns a status code that indicates why this exception occurred.
	 * 
	 * @return a status code
	 * @see StatusCodes
	 */
	public short getStatusCode() {
		return statusCode;
	}

}
