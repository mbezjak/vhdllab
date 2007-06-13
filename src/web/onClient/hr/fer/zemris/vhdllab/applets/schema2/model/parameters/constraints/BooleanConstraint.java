package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;

import java.util.Set;





public class BooleanConstraint implements IParameterConstraint {
	private Set<Object> validset;
	
	
	/**
	 * Konstruira constraint
	 * koji nema ogranicenja na vrijednosti,
	 * tj. moze poprimiti bilo koji cijeli broj. 
	 *
	 */
	public BooleanConstraint() {
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
	public BooleanConstraint(Set<Object> validValues) {
		validset = validValues;
	}
	
	
	
	public boolean checkValue(Object object) {
		if (!(object instanceof Boolean)) return false;
		if (validset == null) return true;
		Boolean val = (Boolean)object;
		if (!validset.contains(val)) return false;
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (!(object instanceof Boolean)) return EConstraintExplanation.WRONG_TYPE;
		return EConstraintExplanation.NO_INFORMATION;
	}

	public Set<Object> getPossibleValues() {
		return validset;
	}
	
	public void setPossibleValues(Set<Object> possibleValues) {
		validset = possibleValues;
	}

}
