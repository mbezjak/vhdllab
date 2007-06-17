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
	
	private class ParameterIterator implements Iterator<IParameter> {
		private Iterator<Entry<String, IParameter>> pit = parameters.entrySet().iterator();
		public boolean hasNext() {
			return pit.hasNext();
		}

		public IParameter next() {
			return pit.next().getValue();
		}

		public void remove() {
			pit.remove();
		}
	}
	
	
	
	/* private fields */
	private Map<String, IParameter> parameters; 
	
	
	
	/* ctors */
	
	public SchemaParameterCollection() {
		parameters = new HashMap<String, IParameter>();
	}
	
	
	
	
	
	/* methods */

	public void addParameter(IParameter parameter) throws DuplicateParameterException {
		String key = parameter.getName();
		if (parameters.containsKey(key)) throw new DuplicateParameterException("Key '" + key + "' is duplicate.");
		parameters.put(key, parameter);
	}

	public int count() {
		return parameters.size();
	}

	public IParameter getParameter(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException("Key '" + key + "' is not found.");
		return parameters.get(key);
	}

	public Object getValue(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException("Key '" + key + "' is not found.");
		return parameters.get(key).getValue();
	}

	public void removeParameter(String key) throws ParameterNotFoundException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException("Key '" + key + "' is not found.");
		parameters.remove(key);
	}

	public void setValue(String key, Object value) throws ParameterNotFoundException, InvalidParameterValueException {
		if (!parameters.containsKey(key)) throw new ParameterNotFoundException("Key '" + key + "' is not found.");
		parameters.get(key).setValue(value);
	}

	public Iterator<IParameter> iterator() {
		return new ParameterIterator();
	}

	public Set<String> getParameterNames() {
		return parameters.keySet();
	}

	public void clear() {
		parameters.clear();
	}
	
	

}



















