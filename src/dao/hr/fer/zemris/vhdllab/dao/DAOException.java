package hr.fer.zemris.vhdllab.dao;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception which is thrown when an exceptional condition occurs
 * in DAO layer.
 */
public class DAOException extends Exception {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -188255765125051430L;

	public DAOException() {
		super();
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Returns a string representation of complete error message.
	 * @return a string representation of complete error message
	 */
	public String getStackTraceAsString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		return sw.toString(); 
	}
	
}
