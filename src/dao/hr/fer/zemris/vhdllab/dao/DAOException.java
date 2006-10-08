package hr.fer.zemris.vhdllab.dao;

/**
 * Exception which is thrown when an exceptional condition occurs
 * in DAO layer.
 */
public class DAOException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -188255765125051430L;

	public DAOException() {
		super();
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
}
