package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.IntegerConstraint;





public class BooleanParameter implements IParameter {
	private Boolean val;
	private IntegerConstraint constraint;
	private boolean generic;
	private String name;
	
	
	
	

	public BooleanParameter(boolean isGeneric, String parameterName) {
		val = new Boolean(false);
		constraint = new IntegerConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	public BooleanParameter(boolean isGeneric, String parameterName, boolean value) {
		val = new Boolean(value);
		constraint = new IntegerConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	
	
	
	
	
	public boolean deserialize(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getName() {
		return name;
	}

	public boolean checkStringValue(String stringValue) {
		if (stringValue == null) return false;
		
		try {
			Boolean.parseBoolean(stringValue);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public IParameterConstraint getConstraint() {
		return constraint;
	}

	public EParamTypes getType() {
		return EParamTypes.BOOLEAN;
	}

	public Object getValue() {
		return val;
	}

	public Boolean getValueAsBoolean() throws ClassCastException {
		return val;
	}

	public Double getValueAsDouble() throws ClassCastException {
		throw new ClassCastException();
	}

	public Integer getValueAsInteger() throws ClassCastException {
		throw new ClassCastException();
	}

	public String getValueAsString() throws ClassCastException {
		throw new ClassCastException();
	}

	public Time getValueAsTime() throws ClassCastException {
		throw new ClassCastException();
	}

	public boolean isParsable() {
		return true;
	}

	public void setAsString(String stringValue) throws InvalidParameterValueException {
		if (stringValue == null) throw new InvalidParameterValueException("Null passed.");
		
		Boolean newval = Boolean.parseBoolean(stringValue);
		
		if (!constraint.checkValue(newval)) {
			throw new InvalidParameterValueException("Correctly parsed, but not allowed by constraint.");
		}
		
		val = newval;
	}

	public void setValue(Object value) throws InvalidParameterValueException {
		if (value == null || !(value instanceof Boolean)) throw new InvalidParameterValueException("Non-integer passed.");
		
		if (!(constraint.checkValue(value))) throw new InvalidParameterValueException("Not allowed by constraint.");
		
		val = (Boolean)value;
	}

	public String getVHDLGenericEntry() {
		return val.toString();
	}

	public boolean isGeneric() {
		return generic;
	}
	
	

	
}











