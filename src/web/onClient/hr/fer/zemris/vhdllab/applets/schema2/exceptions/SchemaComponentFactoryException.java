package hr.fer.zemris.vhdllab.applets.schema2.exceptions;




/**
 * Baca se od strane tvornice sklopova.
 * 
 * @author Axel
 *
 */
public class SchemaComponentFactoryException extends Exception {

	public SchemaComponentFactoryException() {
		super();
	}

	public SchemaComponentFactoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SchemaComponentFactoryException(String arg0) {
		super(arg0);
	}

	public SchemaComponentFactoryException(Throwable arg0) {
		super(arg0);
	}
	
}
