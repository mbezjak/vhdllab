package hr.fer.zemris.vhdllab.service;

/**
 * Exception which is thrown when an exceptional condition occurs
 * in service layer.
 *
 */
public class ServiceException extends Exception {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 6355659630647103263L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
