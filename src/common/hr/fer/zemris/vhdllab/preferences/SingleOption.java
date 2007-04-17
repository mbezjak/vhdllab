package hr.fer.zemris.vhdllab.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;
import hr.fer.zemris.vhdllab.applets.main.event.EventListenerList;
import hr.fer.zemris.vhdllab.preferences.verifier.AlwaysTrueVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SingleOption {
	
	private static final String SERIALIZATION_KEY_NAME = "name";
	private static final String SERIALIZATION_KEY_DESCRIPTION = "description";
	private static final String SERIALIZATION_KEY_VALUE_TYPE = "value.type";
	private static final String SERIALIZATION_KEY_VALUE = "value";
	private static final String SERIALIZATION_KEY_DEFALUT_VALUE = "default.value";
	private static final String SERIALIZATION_KEY_CHOSEN_VALUE = "chosen.value";
	private static final String SERIALIZATION_KEY_EDITABLE_BY_USER = "editable.by.user";
	
	private EventListenerList<OptionListener> optionListener;
	private InputVerifier verifier;
	private Preferences parent;
	private boolean editableByUser;
	private String name;
	private String description;
	private String valueType;
	private List<String> values;
	private String defaultValue;
	private String chosenValue;

	public SingleOption(String name) {
		this(name, PrefConstants.TYPE_STRING);
	}

	public SingleOption(String name, String valueType) {
		this(name, valueType, null);
	}

	public SingleOption(String name, String valueType, String chosenValue) {
		this(name, valueType, null, chosenValue);
	}

	public SingleOption(String name, String valueType, String defaultValue,
			String chosenValue) {
		
		this(name, null, valueType, defaultValue, chosenValue);
	}

	public SingleOption(String name, String description, String valueType,
			String defaultValue, String chosenValue) {
		
		this(name, description, valueType, null, defaultValue, chosenValue);
	}
	
	public SingleOption(String name, String description, String valueType,
			List<String> values, String defaultValue, String chosenValue) {
		
		this(name, description, true, valueType, values, defaultValue, chosenValue);
	}
	
	public SingleOption(String name, String description, boolean editableByUser,
			String valueType, List<String> values, String defaultValue, String chosenValue) {
		
		if(name == null) throw new NullPointerException("Name must not be null.");
		if(valueType == null) throw new NullPointerException("Type of a value must not be null.");
		if(values != null) {
			if(defaultValue == null) {
				throw new NullPointerException("Default value must not be null while a list (values) are not.");
			}
			if(chosenValue == null) {
				throw new NullPointerException("Chosen value must not be null while a list (values) are not.");
			}
			if(!values.contains(defaultValue)) {
				throw new IllegalArgumentException("Default value must be one of value provided in a list (values).");
			}
			if(!values.contains(chosenValue)) {
				throw new IllegalArgumentException("Chosen value must be one of value provided in a list (values).");
			}
		}
		this.optionListener = new EventListenerList<OptionListener>();
		this.verifier = new AlwaysTrueVerifier();
		this.name = name;
		this.description = description;
		this.valueType = valueType;
		this.values = values;
		this.defaultValue = defaultValue;
		this.chosenValue = chosenValue;
		this.editableByUser = editableByUser;
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

	public String getChosenValue() {
		return chosenValue;
	}

	public void setChosenValue(String chosenValue) {
		if(chosenValue == null) throw new NullPointerException("Chosen value must not be null.");
		if(values != null && !values.contains(chosenValue)) {
			throw new IllegalArgumentException("Chosen value must be one of values provided in a list.");
		}
		String oldValue = this.chosenValue;
		this.chosenValue = chosenValue;
		fireOptionChanged(oldValue, chosenValue);
	}

	public List<String> getValues() {
		return Collections.unmodifiableList(values);
	}

	public String getValueType() {
		return valueType;
	}
	
	public Preferences getParent() {
		return parent;
	}
	
	public void setParent(Preferences parent) {
		this.parent = parent;
	}
	
	public boolean isEditableByUser() {
		return editableByUser;
	}
	
	public void setEditableByUser(boolean editableByUser) {
		this.editableByUser = editableByUser;
	}
	
	public InputVerifier getVerifier() {
		return verifier;
	}
	
	public void addOptionListener(OptionListener o) {
		optionListener.add(o);
	}
	
	public void removeOptionListener(OptionListener o) {
		optionListener.remove(o);
	}
	
	public List<OptionListener> getOptionListeners() {
		return optionListener.getListeners();
	}
	
	protected void fireOptionChanged(String oldValue, String newValue) {
		for(OptionListener l : getOptionListeners()) {
			l.optionChanged(oldValue, newValue);
		}
	}

	public String serialize() {
		Properties p = new Properties();
		p.setProperty(SERIALIZATION_KEY_NAME, name);
		p.setProperty(SERIALIZATION_KEY_VALUE_TYPE, valueType);
		p.setProperty(SERIALIZATION_KEY_DESCRIPTION, description);
		p.setProperty(SERIALIZATION_KEY_EDITABLE_BY_USER, String.valueOf(editableByUser));
		if(defaultValue != null) {
			p.setProperty(SERIALIZATION_KEY_DEFALUT_VALUE, defaultValue);
		}
		if(chosenValue != null) {
			p.setProperty(SERIALIZATION_KEY_CHOSEN_VALUE, chosenValue);
		}
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
		boolean editableByUser = Boolean.parseBoolean(p.getProperty(SERIALIZATION_KEY_EDITABLE_BY_USER));
		List<String> values = null;
		if(p.getProperty(SERIALIZATION_KEY_VALUE + ".1") != null) {
			values = new ArrayList<String>();
			for(int i = 1; true; i++) {
				String v = p.getProperty(SERIALIZATION_KEY_VALUE + "." + i);
				if(v == null) break;
				values.add(v);
			}
		}
		return new SingleOption(name, description, editableByUser, valueType, values, defaultValue, chosenValue);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof SingleOption)) return false;
		SingleOption other = (SingleOption) obj;
		
		return name.equalsIgnoreCase(other.name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.toUpperCase().hashCode();
	}

}
