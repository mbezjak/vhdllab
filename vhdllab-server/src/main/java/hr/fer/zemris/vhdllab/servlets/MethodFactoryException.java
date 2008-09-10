package hr.fer.zemris.vhdllab.servlets;

/**
 * Exception which is thrown when an exceptional condition occurs
 * in {@link MethodFactory}.
 * @author Miro Bezjak
 */
public class MethodFactoryException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -5002378139482594929L;

	public MethodFactoryException() {
		super();
	}
	
	public MethodFactoryException(String message) {
		super(message);
	}

	public MethodFactoryException(Throwable cause) {
		super(cause);
	}

	public MethodFactoryException(String message, Throwable cause) {
		super(message, cause);
	}
}