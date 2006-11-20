package hr.fer.zemris.vhdllab.applets.main;

public class ProjectContainerException extends Exception {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2592236289892870034L;

	public ProjectContainerException() {
		super();
	}
	
	public ProjectContainerException(String message) {
		super(message);
	}
	
	public ProjectContainerException(Throwable cause) {
		super(cause);
	}

	public ProjectContainerException(String message, Throwable cause) {
		super(message, cause);
	}

}
