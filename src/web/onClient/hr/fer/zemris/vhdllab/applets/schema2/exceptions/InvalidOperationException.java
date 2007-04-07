package hr.fer.zemris.vhdllab.applets.schema2.exceptions;




/**
 * Baca se u slucaju nevazece operacije,
 * npr. u slucaju da je pozvan undo
 * nad operacijom koja nije undoable. 
 * 
 * @author Axel
 *
 */
public class InvalidOperationException extends Exception {

	public InvalidOperationException() {
		super();
	}

	public InvalidOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidOperationException(String arg0) {
		super(arg0);
	}

	public InvalidOperationException(Throwable arg0) {
		super(arg0);
	}

}
