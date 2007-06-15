package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;

import java.util.Set;





public class IntegerConstraint implements IParameterConstraint {
	private Set<Object> validset;
	
	
	/**
	 * Konstruira constraint
	 * koji nema ogranicenja na vrijednosti,
	 * tj. moze poprimiti bilo koji cijeli broj. 
	 *
	 */
	public IntegerConstraint() {
		validset = null;
	}
	
	
	/**
	 * Stvara constraint kojem je moguce
	 * podesiti koje vrijednosti prihvaca.
	 * 
	 * @param validValues
	 * Null ako su sve vrijednosti dopustene,
	 * neki Set<Object> inace.
	 */
	public IntegerConstraint(Set<Object> validValues) {
		validset = validValues;
	}
	
	
	
	public boolean checkValue(Object object) {
		if (object == null) return false;
		if (!(object instanceof Integer)) return false;
		if (validset == null) return true;
		Integer val = (Integer)object;
		if (!validset.contains(val)) return false;
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (object == null) return EConstraintExplanation.NULL_PASSED;
		if (!(object instanceof Integer)) return EConstraintExplanation.WRONG_TYPE;
		if (validset == null) return EConstraintExplanation.ALLOWED;
		Integer val = (Integer)object;
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















