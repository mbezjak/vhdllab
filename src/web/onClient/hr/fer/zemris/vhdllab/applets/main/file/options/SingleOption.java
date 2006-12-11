package hr.fer.zemris.vhdllab.applets.main.file.options;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SingleOption {
	
	private static final String SERIALIZATION_KEY_NAME = "name";
	private static final String SERIALIZATION_KEY_DESCRIPTION = "description";
	private static final String SERIALIZATION_KEY_VALUE_TYPE = "value.type";
	private static final String SERIALIZATION_KEY_VALUE = "value";
	private static final String SERIALIZATION_KEY_DEFALUT_VALUE = "default.value";
	private static final String SERIALIZATION_KEY_CHOSEN_VALUE = "chosen.value";
	
	private String name;
	private String description;
	private String valueType;
	private List<String> values;
	private String defaultValue;
	private String chosenValue;

	public SingleOption(String name, String description, String valueType, List<String> values, String defaultValue, String chosenValue) {
		if(name == null) throw new NullPointerException("Name must not be null.");
		if(description == null) throw new NullPointerException("Description must not be null.");
		if(valueType == null) throw new NullPointerException("Type of a value must not be null.");
		if(defaultValue == null) throw new NullPointerException("Default value must not be null.");
		if(chosenValue == null) throw new NullPointerException("Chosen value must not be null.");
		if(values != null) {
			if(!values.contains(defaultValue)) {
				throw new IllegalArgumentException("Default value must be one of values provided in a list.");
			}
			if(!values.contains(chosenValue)) {
				throw new IllegalArgumentException("Chosen value must be one of values provided in a list.");
			}
		}

		this.name = name;
		this.description = description;
		this.valueType = valueType;
		this.values = values;
		this.defaultValue = defaultValue;
		this.chosenValue = chosenValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChosenValue() {
		return chosenValue;
	}

	public void setChosenValue(String chosenValue) {
		if(chosenValue == null) throw new NullPointerException("Chosen value must not be null.");
		if(values != null && !values.contains(chosenValue)) {
			throw new IllegalArgumentException("Chosen value must be one of values provided in a list.");
		}
		this.chosenValue = chosenValue;
	}

	public List<String> getValues() {
		return values;
	}

	public String getValueType() {
		return valueType;
	}

	public String serialize() {
		Properties p = new Properties();
		p.setProperty(SERIALIZATION_KEY_NAME, name);
		p.setProperty(SERIALIZATION_KEY_VALUE_TYPE, valueType);
		p.setProperty(SERIALIZATION_KEY_DESCRIPTION, description);
		p.setProperty(SERIALIZATION_KEY_DEFALUT_VALUE, defaultValue);
		p.setProperty(SERIALIZATION_KEY_CHOSEN_VALUE, chosenValue);
		if(values != null) {
			int i = 1;
			for(String v : values) {
				p.setProperty(SERIALIZATION_KEY_VALUE + "." + i, v);
				i++;
			}
		}
		return XMLUtil.serializeProperties(p);
	}
	
	public static SingleOption deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties p = XMLUtil.deserializeProperties(data);
		if(p == null) throw new IllegalArgumentException("Unknown serialization format: data");
		String name = p.getProperty(SERIALIZATION_KEY_NAME);
		String valueType = p.getProperty(SERIALIZATION_KEY_VALUE_TYPE);
		String description = p.getProperty(SERIALIZATION_KEY_DESCRIPTION);
		String defaultValue = p.getProperty(SERIALIZATION_KEY_DEFALUT_VALUE);
		String chosenValue = p.getProperty(SERIALIZATION_KEY_CHOSEN_VALUE);
		List<String> values = null;
		if(p.getProperty(SERIALIZATION_KEY_VALUE + ".1") != null) {
			values = new ArrayList<String>();
			for(int i = 1; true; i++) {
				String v = p.getProperty(SERIALIZATION_KEY_VALUE + "." + i);
				if(v == null) break;
				values.add(v);
			}
		}
		return new SingleOption(name, description, valueType, values, defaultValue, chosenValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof SingleOption)) return false;
		SingleOption other = (SingleOption) obj;
		
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
