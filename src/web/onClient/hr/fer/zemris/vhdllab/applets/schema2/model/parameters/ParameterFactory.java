package hr.fer.zemris.vhdllab.applets.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ParameterWrapper;

import java.util.HashSet;
import java.util.Set;




public class ParameterFactory {
	
	/* static fields */
	public static final String PAR_INTEGER = "INTEGER";
	public static final String PAR_BOOLEAN = "BOOLEAN";
	public static final String PAR_DECIMAL = "DECIMAL";
	public static final String PAR_TEXT = "TEXT";
	public static final String PAR_TIME = "TIME";
	public static final String PAR_CASELESS = "CASELESS";
	public static final String PAR_OBJECT = "OBJECT";


	
	/* private fields */
	
	

	/* ctors */
	
	

	/* methods */
	public static IParameter createParameter(ParameterWrapper parwrapper) {
		IParameter parameter;
		
		String partype = parwrapper.getParamType();
		if (partype.equals(ParameterFactory.PAR_BOOLEAN)) parameter = createBoolean(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_INTEGER)) parameter = createInteger(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_DECIMAL)) parameter = createDecimal(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_TEXT)) parameter = createText(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_TIME)) parameter = createTime(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_CASELESS)) parameter = createCaseless(parwrapper);
		else if (partype.equals(ParameterFactory.PAR_TIME)) parameter = createObject(parwrapper);
		else throw new NotImplementedException("Parameter type '" + partype + "' not implemented.");
		
		return parameter;
	}

	private static IParameter createObject(ParameterWrapper parwrapper) {
		throw new NotImplementedException();
	}

	private static IParameter createCaseless(ParameterWrapper parwrapper) {
		IParameter parameter = new CaselessParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		Caseless val = new Caseless(parwrapper.getValue());
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split("#"); // TODO: vidjeti format stringova
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(new Caseless(sfield[i]));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createTime(ParameterWrapper parwrapper) {
		IParameter parameter = new TimeParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		String val = parwrapper.getValue();
		parameter.setAsString(val);
		
		if (parwrapper.getAllowedValues() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split("#"); // TODO: vidjeti format stringova
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(Time.parseTime(sfield[i]));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createText(ParameterWrapper parwrapper) {
		IParameter parameter = new TextParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		String val = parwrapper.getValue();
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split("#"); // TODO: vidjeti format stringova
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(sfield[i]);
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createDecimal(ParameterWrapper parwrapper) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	private static IParameter createInteger(ParameterWrapper parwrapper) {		
		IParameter parameter = new IntegerParameter(parwrapper.getGeneric(), parwrapper.getName());
		
		Integer val = new Integer(Integer.parseInt(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split("#");
			for (int i = 0; i < sfield.length; i++) {
				try {
					allowed.add(new Integer(Integer.parseInt(sfield[i])));
				} catch (NumberFormatException nfe) {
					continue;
				}
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createBoolean(ParameterWrapper parwrapper) {
		IParameter parameter = new BooleanParameter(parwrapper.getGeneric(), parwrapper.getName());
		
		Boolean val = new Boolean(Boolean.parseBoolean(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split("#");
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(new Boolean(Boolean.parseBoolean(sfield[i])));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

}











