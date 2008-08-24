package hr.fer.zemris.vhdllab.applets.main.event;

/**
 * Thrown to indicate a veto for a specific action.
 * 
 * @author Miro Bezjak
 */
public class ResourceVetoException extends Exception {

	private static final long serialVersionUID = -6403197983536797626L;

	public ResourceVetoException() {
		super();
	}

	public ResourceVetoException(String message) {
		super(message);
	}

	public ResourceVetoException(Throwable cause) {
		super(cause);
	}

	public ResourceVetoException(String message, Throwable cause) {
		super(message, cause);
	}

}
