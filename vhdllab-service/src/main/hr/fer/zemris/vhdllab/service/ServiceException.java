package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.server.ServerException;

/**
 * Exception indicating an exceptional condition in service tier.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ServiceException extends ServerException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a service exception with a specified <code>message</code>.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param message
	 *            a specified message
	 */
	public ServiceException(short statusCode, String message) {
		super(statusCode, message);
	}

	/**
	 * Creates a service exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param cause
	 *            a cause of this exception
	 */
	public ServiceException(ServerException cause) {
		this(cause.getStatusCode(), null, cause);
	}

	/**
	 * Creates a service exception with a specified <code>cause</code> and no
	 * message.
	 * 
	 * @param statusCode
	 *            indicating why this exception occurred
	 * @param cause
	 *            a cause of this exception
	 */
	public ServiceException(short statusCode, Throwable cause) {
		super(statusCode, cause);
	}

	/**
	 * Creates a service exception with a specified <code>message</code> and
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
	public ServiceException(short statusCode, String message, Throwable cause) {
		super(statusCode, message, cause);
	}

}
