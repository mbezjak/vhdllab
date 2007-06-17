package hr.fer.zemris.vhdllab.applets.schema2.exceptions;




/**
 * Baca se od strane tvornice sklopova.
 * 
 * @author Axel
 *
 */
public class SchemaException extends RuntimeException {

	public SchemaException() {
		super();
	}

	public SchemaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SchemaException(String arg0) {
		super(arg0);
	}

	public SchemaException(Throwable arg0) {
		super(arg0);
	}
	
}
