package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.GenericConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints.TextConstraint;


/**
 * Generic u smislu da moze sadrzavati
 * bilo kakav Java objekt koji implementira
 * sucelje IGenericValue.
 * 
 * @author Axel
 *
 * @param <T>
 */
public class GenericParameter<T extends IGenericValue> extends AbstractParameter {

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

	public IParameter copyCtor() {
		GenericParameter<T> param = new GenericParameter<T>(this.name, this.generic, (T)this.value.copyCtor());
		Set<Object> allowed = this.constraint.getPossibleValues();
		allowed = (allowed != null) ? (new HashSet<Object>(allowed)) : (null);
		
		param.constraint = new GenericConstraint<T>(allowed);
		
		return param;
	}

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



	public String getValueClassName() {
		return value.getClass().getName();
	}



	

	


}
















