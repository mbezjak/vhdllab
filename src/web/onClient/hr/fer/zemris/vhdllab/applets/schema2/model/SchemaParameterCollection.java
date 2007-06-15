package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateParameterException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;




public class SchemaParameterCollection implements IParameterCollection {
	private Map<String, IParameter> parameters; 
	
	
	public SchemaParameterCollection() {
		parameters = new HashMap<String, IParameter>();
	}
	
	
	
	
	
	

	public void addParameter(String key, IParameter parameter) throws DuplicateParameterException {
		if (parameters.containsKey(key)) throw new DuplicateParameterException();
		parameters.put(key, parameter);
	}

	public int count() {
		return parameters.size();
	}

	public IParameter getParameter(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException();
		return parameters.get(key);
	}

	public Object getValue(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException();
		return parameters.get(key).getValue();
	}

	public void removeParameter(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException();
		parameters.remove(key);
	}

	public void setValue(String key, Object value) throws ParameterNotFoundException, InvalidParameterValueException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException();
		parameters.get(key).setValue(value);
	}

	public Iterator<Entry<String, IParameter>> iterator() {
		return parameters.entrySet().iterator();
	}

	public Set<String> getParameterNames() {
		return parameters.keySet();
	}
	
	

}
