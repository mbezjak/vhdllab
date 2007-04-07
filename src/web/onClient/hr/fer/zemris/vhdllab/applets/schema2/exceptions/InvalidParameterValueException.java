package hr.fer.zemris.vhdllab.applets.schema2.exceptions;


/**
 * Ova se iznimka baca u slucaju da se neki
 * parametar postavlja na nedozvoljenu vrijednost,
 * ili vrijednost neodgovarajuceg tipa.
 * 
 * @author Axel
 *
 */
public class InvalidParameterValueException extends Exception {

	public InvalidParameterValueException() {
		super();
	}

	public InvalidParameterValueException(String arg0) {
		super(arg0);
	}

	public InvalidParameterValueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidParameterValueException(Throwable arg0) {
		super(arg0);
	}

}
