package hr.fer.zemris.vhdllab.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Preferences {

	private static final String SERIALIZATION_KEY_SINGLE_OPTION = "single.option";
	
	private Map<String, SingleOption> preferences;
	
	public Preferences() {
		preferences = new HashMap<String, SingleOption>();
	}
	
	public void setOption(SingleOption o) {
		if(o == null) throw new NullPointerException("Single option must not be null.");
		preferences.put(o.getName(), o);
	}
	
	public SingleOption getOption(String name) {
		if(name == null) throw new NullPointerException("Name must not be null.");
		return preferences.get(name);
	}
	
	public String serialize() {
		Properties p = new Properties();
		int i = 1;
		for(String s : preferences.keySet()) {
			p.setProperty(SERIALIZATION_KEY_SINGLE_OPTION + "." + i, preferences.get(s).serialize());
			i++;
		}
		return XMLUtil.serializeProperties(p);
	}
	
	public static Preferences deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties p = XMLUtil.deserializeProperties(data);
		if(p == null) throw new IllegalArgumentException("Unknown serialization format: data");
		Preferences pref = new Preferences();
		for(int i = 1; true; i++) {
			String s = p.getProperty(SERIALIZATION_KEY_SINGLE_OPTION + "." + i);
			if(s == null) break;
			SingleOption o = SingleOption.deserialize(s);
			pref.setOption(o);
		}
		return pref;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Preferences)) return false;
		Preferences other = (Preferences) obj;
		
		return preferences.equals(other.preferences);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return preferences.hashCode();
	}
	
}
