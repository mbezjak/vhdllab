package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;



/**
 * Bacaju se kad izvodenje komande bude
 * iz nekog razloga neuspjesno.
 * 
 * @author brijest
 *
 */
public class CommandExecutorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandExecutorException() {
		super();
	}

	public CommandExecutorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CommandExecutorException(String arg0) {
		super(arg0);
	}

	public CommandExecutorException(Throwable arg0) {
		super(arg0);
	}

}
