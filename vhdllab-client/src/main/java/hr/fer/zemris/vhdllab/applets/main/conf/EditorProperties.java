package hr.fer.zemris.vhdllab.applets.main.conf;

public class EditorProperties {

	private String id;
	private String clazz;
	private String fileType;
	private boolean savable;
	private boolean readonly;
	private boolean explicitSave;
	private String explicitSaveClass;
	private boolean singleton;

	public EditorProperties() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		if (fileType != null && fileType.equals("")) {
			fileType = null;
		}
		this.fileType = fileType;
	}

	public boolean isSavable() {
		return savable;
	}

	public void setSavable(boolean savable) {
		this.savable = savable;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean getExplicitSaveValue() {
		return explicitSave;
	}

	public void setExplicitSaveValue(boolean explicitSave) {
		this.explicitSave = explicitSave;
	}

	public String getExplicitSaveClass() {
		return explicitSaveClass;
	}

	public void setExplicitSaveClass(String explicitSaveClass) {
		this.explicitSaveClass = explicitSaveClass;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("editor id=").append(id).append(", class=").append(clazz)
				.append(", filetype=").append(fileType).append(", savable=")
				.append(savable).append(", readonly=").append(readonly).append(
						", explicitsave=").append(explicitSave).append(
						", explicitSaveClass=").append(explicitSaveClass)
				.append(", singleton=").append(singleton);
		return sb.toString();
	}

}
