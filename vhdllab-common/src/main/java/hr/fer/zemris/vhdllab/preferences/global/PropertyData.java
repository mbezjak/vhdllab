package hr.fer.zemris.vhdllab.preferences.global;

import java.util.ArrayList;
import java.util.List;

public class PropertyData {

	private String type;
	private String editor;
	private List<String> values;
	private String defaultValue;

	public PropertyData() {
		values = new ArrayList<String>();
	}

	public void addValue(String value) {
		values.add(value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("propertyData[type=").append(type).append(", editor=")
				.append(editor).append(", {").append(values)
				.append("}, default=").append(defaultValue).append("]");
		return sb.toString();
	}

}
