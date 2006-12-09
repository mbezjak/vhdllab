package hr.fer.zemris.vhdllab.applets.main;

public class UniformAppletException extends Exception {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2592236289892870034L;

	public UniformAppletException() {
		super();
	}
	
	public UniformAppletException(String message) {
		super(message);
	}
	
	public UniformAppletException(Throwable cause) {
		super(cause);
	}

	public UniformAppletException(String message, Throwable cause) {
		super(message, cause);
	}

}
