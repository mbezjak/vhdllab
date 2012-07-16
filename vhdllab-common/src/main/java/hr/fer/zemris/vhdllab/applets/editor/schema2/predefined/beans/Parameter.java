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
package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;


@Deprecated
public class Parameter {
	
	private Boolean generic;
	private String type;
	private String name;
	private String value;
	private String allowedValueSet;

	public Parameter() {
	}

	public Boolean isGeneric() {
		return generic;
	}

	public void setGeneric(Boolean generic) {
		this.generic = generic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type.trim().equals("")) {
			type = null;
		}
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.trim().equals("")) {
			name = null;
		}
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value.trim().equals("")) {
			value = null;
		}
		this.value = value;
	}

	public String getAllowedValueSet() {
		return allowedValueSet;
	}

	public void setAllowedValueSet(String allowedValueSet) {
		if (allowedValueSet.trim().equals("")) {
			allowedValueSet = null;
		}
		this.allowedValueSet = allowedValueSet;
	}
	
	@Override
	public String toString() {
		return "Parameter {" +
			generic + ", " + 
			type + ", " + 
			name + ", " + 
			value + ", " + 
			allowedValueSet + "}";
	}

}
