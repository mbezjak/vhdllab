package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Parameter;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;




public class ParameterFactory {
	/* static fields */

	/* private fields */

	/* ctors */

	/* methods */
	public IParameter createParameter(Parameter parwrapper) {
		IParameter parameter;
		
		String partype = parwrapper.getType();
		if (partype.equals(Parameter.PAR_BOOLEAN)) parameter = createBoolean(parwrapper);
		else if (partype.equals(Parameter.PAR_INTEGER)) parameter = createInteger(parwrapper);
		else if (partype.equals(Parameter.PAR_DECIMAL)) parameter = createDecimal(parwrapper);
		else if (partype.equals(Parameter.PAR_TEXT)) parameter = createText(parwrapper);
		else if (partype.equals(Parameter.PAR_TIME)) parameter = createTime(parwrapper);
		else throw new NotImplementedException("Parameter type '" + partype + "' not implemented.");
		
		return parameter;
	}

	private IParameter createTime(Parameter parwrapper) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	private IParameter createText(Parameter parwrapper) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	private IParameter createDecimal(Parameter parwrapper) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	private IParameter createInteger(Parameter parwrapper) {		
		IParameter parameter = new IntegerParameter(parwrapper.isGeneric(), parwrapper.getName());
		
		Integer val = new Integer(Integer.parseInt(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValueSet() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValueSet().split(" ");
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(new Integer(Integer.parseInt(sfield[i])));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		return parameter;
	}

	private IParameter createBoolean(Parameter parwrapper) {
		IParameter parameter = new BooleanParameter(parwrapper.isGeneric(), parwrapper.getName());
		
		Boolean val = new Boolean(Boolean.parseBoolean(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValueSet() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValueSet().split(" ");
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(new Boolean(Boolean.parseBoolean(sfield[i])));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		return parameter;
	}

}











