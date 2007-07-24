package hr.fer.zemris.vhdllab.preferences.global;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Preferences {

	private Map<String, Property> properties;

	public Preferences() {
		properties = new HashMap<String, Property>();
	}

	public void addProperty(Property p) {
		properties.put(p.getId(), p);
	}
	
	public Property getProperty(String id) {
		return properties.get(id);
	}
	
	public Collection<Property> getProperties() {
		return properties.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + properties.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Preferences other = (Preferences) obj;
		if (!properties.equals(other.properties))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1000 + 500 * properties.size());
		sb.append("preferences[").append(",\n");
		for (Property p : properties.values()) {
			sb.append(p).append("\n");
		}
		sb.append("]");
		return sb.toString();
	}

}
