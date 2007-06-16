package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.CaselessConstraint;

public class CaselessParameter implements IParameter {
	
	/* static fields */
	
	

	/* private fields */
	private String name;
	private Caseless value;
	private boolean generic;
	private CaselessConstraint constraint;
	

	/* ctors */
	
	public CaselessParameter(String parameterName, boolean isGeneric) {
		name = parameterName;
		value = new Caseless("");
		generic = isGeneric;
		constraint = new CaselessConstraint();
	}
	
	public CaselessParameter(String parameterName, boolean isGeneric, Caseless initialValue) {
		name = parameterName;
		value = initialValue;
		generic = isGeneric;
		constraint = new CaselessConstraint();
	}
	

	/* methods */

	public boolean checkStringValue(String stringValue) {
		return true;
	}

	public IParameterConstraint getConstraint() {
		return constraint;
	}

	public String getName() {
		return name;
	}

	public EParamTypes getType() {
		return EParamTypes.CASELESS;
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
		return value.toString();
	}

	public Time getValueAsTime() throws ClassCastException {
		throw new ClassCastException();
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
		
		Caseless nval = new Caseless(stringValue);
		if (!(constraint.checkValue(nval)))
				throw new InvalidParameterValueException("Not allowed by constraint.");
		
		value = new Caseless(stringValue);
	}

	public void setValue(Object val) throws InvalidParameterValueException {
		if (val == null || !(val instanceof Caseless))
			throw new InvalidParameterValueException("Non-string passed.");
		
		if (!(constraint.checkValue(val)))
			throw new InvalidParameterValueException("Not allowed by constraint.");
		
		value = (Caseless)val;
	}

	public String getValueClassName() {
		return Caseless.class.getName();
	}
	

}















