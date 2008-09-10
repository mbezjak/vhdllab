package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;


/**
 * Baca se kad parametar vezan za
 * neki sklop ili zicu nije naden.
 * 
 * @author Axel
 *
 */
public class ParameterNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterNotFoundException() {
		super();
	}

	public ParameterNotFoundException(String arg0) {
		super(arg0);
	}

	public ParameterNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ParameterNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
