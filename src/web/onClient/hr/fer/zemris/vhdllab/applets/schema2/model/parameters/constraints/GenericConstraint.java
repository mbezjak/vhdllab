package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISerializable;

import java.util.Set;





public class GenericConstraint<T extends ISerializable> implements IParameterConstraint {
	
	/* static fields */

	/* private fields */
	private Set<Object> allowed;

	
	
	/* ctors */
	
	public GenericConstraint() {
		allowed = null;
	}
	
	public GenericConstraint(Set<Object> allowedValues) {
		allowed = allowedValues;
	}
	
	

	/* methods */

	public boolean checkValue(Object object) {
		if (object == null) return false;
		
		try {
			T val = (T)object;
			if (allowed == null) return true;
			if (!(allowed.contains(val))) return false;
		} catch (ClassCastException cce) {
			return false;
		}
		
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (object == null) return EConstraintExplanation.NULL_PASSED;
		
		try {
			T val = (T)object;
			if (allowed == null) return EConstraintExplanation.ALLOWED;
			if (!(allowed.contains(val))) return EConstraintExplanation.CONSTRAINT_BANNED;
		} catch (ClassCastException cce) {
			return EConstraintExplanation.WRONG_TYPE;
		}
		
		return EConstraintExplanation.ALLOWED;
	}

	public Set<Object> getPossibleValues() {
		return allowed;
	}

	public void setPossibleValues(Set<Object> possibleValues) {
		allowed = possibleValues;
	}
	

}
