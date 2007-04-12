package hr.fer.zemris.vhdllab.applets.schema2.exceptions;







/**
 * Baca se u slucaju da metoda nije
 * implementirana.
 * 
 * @author brijest
 *
 */
public class NotImplementedException extends RuntimeException {

	public NotImplementedException() {
		super();
	}

	public NotImplementedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotImplementedException(String arg0) {
		super(arg0);
	}

	public NotImplementedException(Throwable arg0) {
		super(arg0);
	}
	
}
