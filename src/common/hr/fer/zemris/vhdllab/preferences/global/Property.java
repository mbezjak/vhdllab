package hr.fer.zemris.vhdllab.preferences.global;

public class Property {

	private String id;
	private String type;
	private String description;
	private String tooltip;
	private boolean editableBySystem;
	private boolean editableByUser;
	private PropertyData data;

	public Property() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean isEditableBySystem() {
		return editableBySystem;
	}

	public void setEditableBySystem(boolean editableBySystem) {
		this.editableBySystem = editableBySystem;
	}

	public boolean isEditableByUser() {
		return editableByUser;
	}

	public void setEditableByUser(boolean editableByUser) {
		this.editableByUser = editableByUser;
	}

	public PropertyData getData() {
		return data;
	}

	public void setData(PropertyData data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.toLowerCase().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Property))
			return false;
		Property other = (Property) obj;
		return this.id.equalsIgnoreCase(other.id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(500);
		sb.append("property[").append("id=").append(id)
				.append(", type=").append(type)
				.append(", description=").append(description)
				.append(", tooltip=").append(tooltip)
				.append(", editableBySystem=").append(editableBySystem)
				.append(", editableByUser=").append(editableByUser)
				.append(", data=").append(data).append("]");
		return sb.toString();
	}
}
