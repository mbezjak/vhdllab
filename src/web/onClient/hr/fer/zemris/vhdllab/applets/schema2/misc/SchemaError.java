package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;


/**
 * Klasa koja enkapsulira pogresku.
 * 
 * @author Axel
 *
 */
public class SchemaError {
	private EErrorTypes errcode;
	private String message;
	
	public SchemaError() {
		setErrorcode(EErrorTypes.NO_ERROR);
		setMessage(null);
	}
	
	public SchemaError(EErrorTypes errorCode) {
		errcode = errorCode;
	}
	
	public SchemaError(EErrorTypes errorCode, String errorMessage) {
		setErrorcode(errorCode);
		setMessage(errorMessage);
	}

	public void setErrorcode(EErrorTypes errorcode) {
		this.errcode = errorcode;
	}

	public EErrorTypes getErrorcode() {
		return errcode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
