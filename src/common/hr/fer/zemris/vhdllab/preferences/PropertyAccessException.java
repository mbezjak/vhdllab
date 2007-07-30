package hr.fer.zemris.vhdllab.preferences;

public class PropertyAccessException extends Exception {

	private static final long serialVersionUID = 4360293517731039816L;

	public PropertyAccessException() {
		super();
	}
	
	public PropertyAccessException(String message) {
		super(message);
	}
	
	public PropertyAccessException(Throwable cause) {
		super(cause);
	}

	public PropertyAccessException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
