package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.constraints;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;





public class IntegerConstraint implements IParameterConstraint {
	private Set<Object> validset;
	
	
	/**
	 * Konstruira constraint
	 * koji nema ogranicenja na vrijednosti,
	 * tj. moze poprimiti bilo koji cijeli broj. 
	 *
	 */
	public IntegerConstraint() {
		validset = new HashSet<Object>();
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
		if (!(object instanceof Integer)) return false;
		if (validset == null) return true;
		Integer val = (Integer)object;
		if (!validset.contains(val)) return false;
		return true;
	}

	public EConstraintExplanation getExplanation(Object object) {
		if (!(object instanceof Integer)) return EConstraintExplanation.WRONG_TYPE;
		return EConstraintExplanation.NO_INFORMATION;
	}

	public Set<Object> getPossibleValues() {
		return validset;
	}

}
