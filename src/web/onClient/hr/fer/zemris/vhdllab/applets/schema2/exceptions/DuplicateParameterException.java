package hr.fer.zemris.vhdllab.applets.schema2.exceptions;



/**
 * Baca se u slucaju dodavanja vec postojeceg
 * parametra u parametarsku kolekciju.
 * 
 * @author Axel
 *
 */
public class DuplicateParameterException extends RuntimeException {

	public DuplicateParameterException() {
		super();
	}

	public DuplicateParameterException(String arg0) {
		super(arg0);
	}

	public DuplicateParameterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateParameterException(Throwable arg0) {
		super(arg0);
	}

}
