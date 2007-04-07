package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;


/**
 * Klasa koja enkapsulira pogresku.
 * 
 * @author Axel
 *
 */
public class SchemaError {
	private EErrorTypes errorcode;
	private String message;
	
	public SchemaError() {
		setErrorcode(EErrorTypes.NO_ERROR);
		setMessage(null);
	}
	
	public SchemaError(EErrorTypes errorCode) {
		errorCode = errorCode;
	}
	
	public SchemaError(EErrorTypes errorCode, String errorMessage) {
		setErrorcode(errorCode);
		setMessage(errorMessage);
	}

	private void setErrorcode(EErrorTypes errorcode) {
		this.errorcode = errorcode;
	}

	private EErrorTypes getErrorcode() {
		return errorcode;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	private String getMessage() {
		return message;
	}
}
