package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;




public class ParameterFactory {
	
	/* static fields */
	public static final String PAR_INTEGER = EParamTypes.INTEGER.toString();
	public static final String PAR_BOOLEAN = EParamTypes.BOOLEAN.toString();
	public static final String PAR_DECIMAL = EParamTypes.DECIMAL.toString();
	public static final String PAR_TEXT = EParamTypes.TEXT.toString();
	public static final String PAR_TIME = EParamTypes.TIME.toString();
	public static final String PAR_CASELESS = EParamTypes.CASELESS.toString();
	public static final String PAR_OBJECT = EParamTypes.OBJECT.toString();


	
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
		else if (partype.equals(ParameterFactory.PAR_OBJECT)) parameter = createObject(parwrapper);
		else throw new NotImplementedException("Parameter type '" + partype + "' not implemented.");
		
		return parameter;
	}

	@SuppressWarnings("unchecked")
	private static IGenericValue createGenericValue(ParameterWrapper parwrapper) {
		String valclass = parwrapper.getValueType();
		Object valobj = null;
		IGenericValue genval = null;
		
		try {
			Class cls = Class.forName(valclass);
			Constructor<IGenericValue> ctor = cls.getConstructor((Class[])null);
			valobj = ctor.newInstance((Object[])null);
			genval = (IGenericValue)valobj;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot instantiate parameter " +
					"value - is value IGenericValue?", e);
		}
		
		return genval;
	}
	
	@SuppressWarnings("unchecked")
	private static IParameter createObject(ParameterWrapper parwrapper) {
		IParameter parameter = new GenericParameter(parwrapper.getName(), parwrapper.getGeneric(), null);
		
		// instantiate value object
		IGenericValue genval = createGenericValue(parwrapper);
		
		// deserialize IGenericValue object
		genval.deserialize(parwrapper.getValue());
		
		// now set value
		parameter.setValue(genval);
		
		// set constraint's allowedValuesSet
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				genval = createGenericValue(parwrapper);
				genval.deserialize(sfield[i]);
				allowed.add(genval);
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createCaseless(ParameterWrapper parwrapper) {
		IParameter parameter = new CaselessParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		Caseless val = new Caseless(parwrapper.getValue());
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(new Caseless(sfield[i]));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createTime(ParameterWrapper parwrapper) {
		IParameter parameter = new TimeParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		String val = parwrapper.getValue();
		parameter.setAsString(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(Time.parseTime(sfield[i]));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createText(ParameterWrapper parwrapper) {
		IParameter parameter = new TextParameter(parwrapper.getName(), parwrapper.getGeneric());
		
		String val = parwrapper.getValue();
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(sfield[i]);
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createDecimal(ParameterWrapper parwrapper) {
		IParameter parameter = new DecimalParameter(parwrapper.getGeneric(), parwrapper.getName());
		
		Double val = new Double(Double.parseDouble(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				try {
					allowed.add(new Double(Double.parseDouble(sfield[i])));
				} catch (NumberFormatException nfe) {
					continue;
				}
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createInteger(ParameterWrapper parwrapper) {		
		IParameter parameter = new IntegerParameter(parwrapper.getGeneric(), parwrapper.getName());
		
		Integer val = new Integer(Integer.parseInt(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				try {
					allowed.add(Integer.valueOf(Integer.parseInt(sfield[i])));
				} catch (NumberFormatException nfe) {
					continue;
				}
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

	private static IParameter createBoolean(ParameterWrapper parwrapper) {
		IParameter parameter = new BooleanParameter(parwrapper.getGeneric(), parwrapper.getName());
		
		Boolean val = new Boolean(Boolean.parseBoolean(parwrapper.getValue()));
		parameter.setValue(val);
		
		if (parwrapper.getAllowedValues() != null && !parwrapper.getAllowedValues().equals("")) {
			Set<Object> allowed = new HashSet<Object>();
			String[] sfield = parwrapper.getAllowedValues().split(Constants.ALLOWED_SET_DIVIDER);
			for (int i = 0; i < sfield.length; i++) {
				allowed.add(Boolean.valueOf(Boolean.parseBoolean(sfield[i])));
			}
			parameter.getConstraint().setPossibleValues(allowed);
		}
		
		String eventName = parwrapper.getEventName();
		if (eventName != null && !eventName.trim().equals("")) parameter.setParameterEvent(eventName);
		
		return parameter;
	}

}











