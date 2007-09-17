package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.DecimalConstraint;

import java.util.HashSet;
import java.util.Set;





public class DecimalParameter extends AbstractParameter {
	private Double val;
	private DecimalConstraint constraint;
	private boolean generic;
	private String name;
	
	
	
	
	

	public DecimalParameter(boolean isGeneric, String parameterName) {
		val = new Double(0);
		constraint = new DecimalConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	public DecimalParameter(boolean isGeneric, String parameterName, int value) {
		val = new Double(value);
		constraint = new DecimalConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	
	
	
	
	
	

	public IParameter copyCtor() {
		DecimalParameter tp = new DecimalParameter(this.generic, this.name);
		Set<Object> allowed = this.constraint.getPossibleValues();
		allowed = (allowed != null) ? (new HashSet<Object>(allowed)) : (null);
		
		tp.val = this.val;
		tp.constraint = new DecimalConstraint(allowed);
		tp.paramevent = (this.paramevent != null) ? (this.paramevent.copyCtor()) : (null);
		
		return tp;
	}
	
	public String getName() {
		return name;
	}

	public boolean checkStringValue(String stringValue) {
		if (stringValue == null) return false;
		
		try {
			Integer.parseInt(stringValue);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public IParameterConstraint getConstraint() {
		return constraint;
	}

	public EParamTypes getType() {
		return EParamTypes.DECIMAL;
	}

	public Object getValue() {
		return val;
	}

	public Boolean getValueAsBoolean() throws ClassCastException {
		throw new ClassCastException();
	}

	public Double getValueAsDouble() throws ClassCastException {
		return val;
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
		
		Double newval = Double.parseDouble(stringValue);
		
		if (!constraint.checkValue(newval)) {
			throw new InvalidParameterValueException("Correctly parsed, but not allowed by constraint.");
		}
		
		val = newval;
	}

	public void setValue(Object value) throws InvalidParameterValueException {
		if (value == null || !(value instanceof Double))
			throw new InvalidParameterValueException("Non-double passed.");
		
		if (!(constraint.checkValue(value)))
			throw new InvalidParameterValueException("Not allowed by constraint.");
		
		val = (Double)value;
	}

	public String getVHDLGenericEntry() {
		return val.toString();
	}

	public boolean isGeneric() {
		return generic;
	}

	public String getValueClassName() {
		return Double.class.getName();
	}
	
	

	
}











