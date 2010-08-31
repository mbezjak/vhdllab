/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;


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
		errcode = EErrorTypes.UNKNOWN_TYPE;
		message = null;
	}
	
	public SchemaError(EErrorTypes errorCode) {
		errcode = errorCode;
	}
	
	public SchemaError(EErrorTypes errorCode, String errorMessage) {
		errcode = errorCode;
		message = errorMessage;
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

	@Override
	public String toString() {
		return errcode.toString() + " " + message;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof SchemaError)) return false;
		SchemaError other = (SchemaError)obj;
		return other.errcode.equals(this.errcode) && other.message.equals(this.message);
	}

	@Override
	public int hashCode() {
		return errcode.hashCode() << 16 + message.hashCode();
	}
	
	
	
}









