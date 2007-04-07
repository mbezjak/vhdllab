package hr.fer.zemris.vhdllab.applets.schema2.exceptions;



/**
 * Bacaju se od strane ICommandExecutora.
 * 
 * @author brijest
 *
 */
public class CommandExecutorException extends Exception {

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
