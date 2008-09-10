package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;




/**
 * Baca se u slucaju nevazece operacije,
 * npr. u slucaju da je pozvan undo
 * nad operacijom koja nije undoable. 
 * 
 * @author Axel
 *
 */
public class InvalidCommandOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCommandOperationException() {
		super();
	}

	public InvalidCommandOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidCommandOperationException(String arg0) {
		super(arg0);
	}

	public InvalidCommandOperationException(Throwable arg0) {
		super(arg0);
	}

}
