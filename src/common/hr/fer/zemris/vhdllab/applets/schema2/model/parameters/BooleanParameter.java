package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.BooleanConstraint;

import java.util.HashSet;
import java.util.Set;





public class BooleanParameter extends AbstractParameter {
	private Boolean val;
	private BooleanConstraint constraint;
	private boolean generic;
	private String name;
	
	
	
	

	public BooleanParameter(boolean isGeneric, String parameterName) {
		val = new Boolean(false);
		constraint = new BooleanConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	public BooleanParameter(boolean isGeneric, String parameterName, boolean value) {
		val = new Boolean(value);
		constraint = new BooleanConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	
	
	
	

	public IParameter copyCtor() {
		BooleanParameter tp = new BooleanParameter(this.generic, this.name);
		Set<Object> allowed = this.constraint.getPossibleValues();
		allowed = (allowed != null) ? (new HashSet<Object>(allowed)) : (null);
		
		tp.val = this.val;
		tp.constraint = new BooleanConstraint(allowed);
		tp.paramevent = (this.paramevent != null) ? (this.paramevent.copyCtor()) : (null);
		
		return tp;
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

	public String getValueClassName() {
		return Boolean.class.getName();
	}
	
	

	
}











