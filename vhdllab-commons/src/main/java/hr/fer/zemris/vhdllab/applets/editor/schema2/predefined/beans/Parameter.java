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
