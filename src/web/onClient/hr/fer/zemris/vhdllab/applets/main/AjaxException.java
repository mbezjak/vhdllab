package hr.fer.zemris.vhdllab.applets.main;

public class AjaxException extends Exception {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2592236289892870034L;

	public AjaxException() {
		super();
	}
	
	public AjaxException(String message) {
		super(message);
	}
	
	public AjaxException(Throwable cause) {
		super(cause);
	}

	public AjaxException(String message, Throwable cause) {
		super(message, cause);
	}

}
