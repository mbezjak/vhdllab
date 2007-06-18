package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.ETimeMetrics;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.TimeFormatException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.TimeConstraint;

public class TimeParameter extends AbstractParameter {
	
	/* static fields */
	
	

	/* private fields */
	private String name;
	private Time value;
	private boolean generic;
	private TimeConstraint constraint;
	

	/* ctors */
	
	public TimeParameter(String parameterName, boolean isGeneric) {
		name = parameterName;
		value = new Time(0, ETimeMetrics.sec);
		generic = isGeneric;
		constraint = new TimeConstraint();
	}
	
	public TimeParameter(String parameterName, boolean isGeneric, Time initialValue) {
		name = parameterName;
		value = initialValue;
		generic = isGeneric;
		constraint = new TimeConstraint();
	}
	

	/* methods */
	
	public IParameter copyCtor() {
		throw new NotImplementedException();
	}

	public boolean checkStringValue(String stringValue) {
		try {
			Time.parseTime(stringValue);
		} catch (TimeFormatException tfe) {
			return false;
		}
		return true;
	}

	public IParameterConstraint getConstraint() {
		return constraint;
	}

	public String getName() {
		return name;
	}

	public EParamTypes getType() {
		return EParamTypes.TIME;
	}

	public Object getValue() {
		return value;
	}

	public Boolean getValueAsBoolean() throws ClassCastException {
		throw new ClassCastException();
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
		return (Time)value;
	}

	public String getVHDLGenericEntry() {
		return value.toString();
	}

	public boolean isGeneric() {
		return generic;
	}

	public boolean isParsable() {
		return true;
	}

	public void setAsString(String stringValue) throws InvalidParameterValueException {
		if (stringValue == null) throw new InvalidParameterValueException("Null passed.");
		
		Time nval = Time.parseTime(stringValue);
		if (!(constraint.checkValue(nval)))
				throw new InvalidParameterValueException("Not allowed by constraint.");
		
		value = nval;
	}

	public void setValue(Object val) throws InvalidParameterValueException {
		if (val == null || !(val instanceof Time))
			throw new InvalidParameterValueException("Non-time passed.");
		
		if (!(constraint.checkValue(val)))
			throw new InvalidParameterValueException("Not allowed by constraint.");
		
		value = (Time)val;
	}

	public String getValueClassName() {
		return Time.class.getName();
	}
	

}















