package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.GenericConstraint;


/**
 * Generic u smislu da moze sadrzavati
 * bilo kakav Java objekt koji implementira
 * sucelje ISerializable.
 * 
 * @author Axel
 *
 * @param <T>
 */
public class GenericParameter<T extends ISerializable> implements IParameter {

	/* static fields */

	/* private fields */
	String name;
	T value;
	boolean generic;
	GenericConstraint<T> constraint;
	

	/* ctors */
	
	public GenericParameter(String parameterName, boolean isGeneric, T initialValue) {
		name = parameterName;
		generic = isGeneric;
		value = initialValue;
		constraint = new GenericConstraint<T>();
	}
	
	

	/* methods */
	
	public boolean checkStringValue(String stringValue) {
		return false;
	}

	public IParameterConstraint getConstraint() {
		return constraint;
	}

	public String getName() {
		return name;
	}

	public EParamTypes getType() {
		return EParamTypes.OBJECT;
	}

	/**
	 * Returns the parametrized object's
	 * toString() method return value.
	 */
	public String getVHDLGenericEntry() {
		return value.toString();
	}

	public Object getValue() {
		return value;
	}

	public Boolean getValueAsBoolean() throws ClassCastException {
		return (Boolean)((Object)value);
	}

	public Double getValueAsDouble() throws ClassCastException {
		return (Double)((Object)value);
	}

	public Integer getValueAsInteger() throws ClassCastException {
		return (Integer)((Object)value);
	}

	public String getValueAsString() throws ClassCastException {
		return (String)((Object)value);
	}

	public Time getValueAsTime() throws ClassCastException {
		return (Time)((Object)value);
	}

	public boolean isGeneric() {
		return generic;
	}

	public boolean isParsable() {
		return false;
	}

	public void setAsString(String stringValue) throws InvalidParameterValueException {
		throw new InvalidParameterValueException("Generic parameters cannot be set as string.");
	}

	public void setValue(Object newvalue) throws InvalidParameterValueException {
		T tval;
		try {
			tval = (T)newvalue;
			
			if (!constraint.checkValue(newvalue))
				throw new InvalidParameterValueException("Value not allowed by constraint.");
			
			value = tval;
		} catch (ClassCastException cce) {
			throw new InvalidParameterValueException("Value is not of right type.");
		}
	}

	public boolean deserialize(String code) {
		// TODO Auto-generated method stub
		return false;
	}

	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
















