package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.constraints.IntegerConstraint;

import java.util.HashSet;
import java.util.Set;





public class IntegerParameter extends AbstractParameter {
	private Integer val;
	private IntegerConstraint constraint;
	private boolean generic;
	private String name;
	
	
	
	
	

	public IntegerParameter(boolean isGeneric, String parameterName) {
		val = Integer.valueOf(0);
		constraint = new IntegerConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	public IntegerParameter(boolean isGeneric, String parameterName, int value) {
		val = Integer.valueOf(value);
		constraint = new IntegerConstraint();
		name = parameterName;
		generic = isGeneric;
	}
	
	
	
	
	
	
	

	public IParameter copyCtor() {
		IntegerParameter tp = new IntegerParameter(this.generic, this.name);
		Set<Object> allowed = this.constraint.getPossibleValues();
		allowed = (allowed != null) ? (new HashSet<Object>(allowed)) : (null);
		
		tp.val = this.val;
		tp.constraint = new IntegerConstraint(allowed);
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
		return EParamTypes.INTEGER;
	}

	public Object getValue() {
		return val;
	}

	public Boolean getValueAsBoolean() throws ClassCastException {
		throw new ClassCastException();
	}

	public Double getValueAsDouble() throws ClassCastException {
		throw new ClassCastException();
	}

	public Integer getValueAsInteger() throws ClassCastException {
		return val;
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
		
		Integer newval = Integer.parseInt(stringValue);
		
		if (!constraint.checkValue(newval)) {
			throw new InvalidParameterValueException("Correctly parsed, but not allowed by constraint.");
		}
		
		val = newval;
	}

	public void setValue(Object value) throws InvalidParameterValueException {
		if (value == null || !(value instanceof Integer))
			throw new InvalidParameterValueException("Non-integer passed.");
		
		if (!(constraint.checkValue(value)))
			throw new InvalidParameterValueException("Not allowed by constraint.");
		
		val = (Integer)value;
	}

	public String getVHDLGenericEntry() {
		return val.toString();
	}

	public boolean isGeneric() {
		return generic;
	}

	public String getValueClassName() {
		return Integer.class.getName();
	}
	
	

	
}











