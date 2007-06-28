package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;

import java.util.Set;





public class DecimalConstraint implements IParameterConstraint {
	private Set<Object> validset;
	
	
	/**
	 * Konstruira constraint
	 * koji nema ogranicenja na vrijednosti,
	 * tj. moze poprimiti bilo koji cijeli broj. 
	 *
	 */
	public DecimalConstraint() {
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
	public DecimalConstraint(Set<Object> validValues) {
		validset = validValues;
	}
	
	
	
	public boolean checkValue(Object object) {
		if (object == null) return false;
		if (!(object instanceof Double)) return false;
		if (validset == null) return true;
		Double val = (Double)object;
		if (!validset.contains(val)) return false;
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (object == null) return EConstraintExplanation.NULL_PASSED;
		if (!(object instanceof Double)) return EConstraintExplanation.WRONG_TYPE;
		if (validset == null) return EConstraintExplanation.ALLOWED;
		Double val = (Double)object;
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















