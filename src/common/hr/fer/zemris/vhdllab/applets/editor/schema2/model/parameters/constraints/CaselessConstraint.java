package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.constraints;

import java.util.Set;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;


public class CaselessConstraint implements IParameterConstraint {
	
	/* static fields */
	

	/* private fields */
	private Set<Object> validset;
	

	/* ctors */
	
	public CaselessConstraint() {
		validset = null;
	}
	
	public CaselessConstraint(Set<Object> validValues) {
		validset = validValues;
	}
	

	
	
	/* methods */

	public boolean checkValue(Object object) {
		if (object == null) return false;
		if (!(object instanceof Caseless)) return false;
		if (validset == null) return true;
		Caseless val = (Caseless)object;
		if (!(validset.contains(val))) return false;
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (object == null) return EConstraintExplanation.NULL_PASSED;
		if (!(object instanceof Caseless)) return EConstraintExplanation.WRONG_TYPE;
		if (validset == null) return EConstraintExplanation.ALLOWED;
		Caseless val = (Caseless)object;
		if (!(validset.contains(val))) return EConstraintExplanation.CONSTRAINT_BANNED;
		return EConstraintExplanation.ALLOWED;
	}

	public Set<Object> getPossibleValues() {
		return validset;
	}

	public void setPossibleValues(Set<Object> possibleValues) {
		validset = possibleValues;
	}
	
}












