package hr.fer.zemris.vhdllab.applets.schema2.exceptions;






/**
 * Baca se kad nije moguce dodati zicu ili
 * komponentu jer bi se one preklapale.
 * 
 * @author Axel
 *
 */
public class OverlapException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverlapException() {
		super();
	}

	public OverlapException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OverlapException(String arg0) {
		super(arg0);
	}

	public OverlapException(Throwable arg0) {
		super(arg0);
	}
	
}
