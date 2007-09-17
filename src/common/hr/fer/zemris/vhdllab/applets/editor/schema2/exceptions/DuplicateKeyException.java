package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;



/**
 * Bacaju se kad postoji duplikat kljuca
 * u nekoj od kolekcija.
 * 
 * @author brijest
 *
 */
public class DuplicateKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateKeyException() {
		super();
	}

	public DuplicateKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateKeyException(String arg0) {
		super(arg0);
	}

	public DuplicateKeyException(Throwable arg0) {
		super(arg0);
	}

}
