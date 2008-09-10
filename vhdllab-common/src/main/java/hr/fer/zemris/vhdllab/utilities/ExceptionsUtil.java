package hr.fer.zemris.vhdllab.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Utility class for exceptions.
 * 
 * @author Miro Bezjak
 */
public class ExceptionsUtil {

	/**
	 * Returns a string representation of throwable <code>cause</code>.
	 * 
	 * @param cause
	 *            a throwable
	 * @return a string representation of <code>cause</code>
	 * @throws NullPointerException
	 *             if <code>cause</code> is <code>null</code>
	 */
	public static String printStackTrace(Throwable cause) {
		if (cause == null) {
			throw new NullPointerException("Cause cant be null");
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		cause.printStackTrace(pw);
		return sw.toString();
	}

}
