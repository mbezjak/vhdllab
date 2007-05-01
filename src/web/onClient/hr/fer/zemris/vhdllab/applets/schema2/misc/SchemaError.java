package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;


/**
 * Klasa koja enkapsulira pogresku.
 * 
 * @author Axel
 *
 */
public final class SchemaError {
	private EErrorTypes errcode;
	private String message;
	
	public SchemaError() {
		setErrorcode(EErrorTypes.UNKNOWN_TYPE);
		setMessage(null);
	}
	
	public SchemaError(EErrorTypes errorCode) {
		errcode = errorCode;
	}
	
	public SchemaError(EErrorTypes errorCode, String errorMessage) {
		setErrorcode(errorCode);
		setMessage(errorMessage);
	}

	public final void setErrorcode(EErrorTypes errorcode) {
		this.errcode = errorcode;
	}

	public final EErrorTypes getErrorcode() {
		return errcode;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	public final String getMessage() {
		return message;
	}
}






