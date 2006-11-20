package hr.fer.zemris.vhdllab.applets.main;

public class CacheException extends Exception {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2592236289892870034L;

	public CacheException() {
		super();
	}
	
	public CacheException(String message) {
		super(message);
	}
	
	public CacheException(Throwable cause) {
		super(cause);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

}
