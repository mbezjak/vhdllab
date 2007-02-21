package hr.fer.zemris.vhdllab.applets.main.conf;

public class EditorProperties {

	private String name;
	private String fileType;
	private String clazz;
	private boolean savable;
	private boolean readonly;
	private boolean explicitSave;
	private String explicitSaveClass;
	
	public EditorProperties() {}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
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

	public boolean isExplicitSave() {
		return explicitSave;
	}
	public void setExplicitSave(boolean explicitSave) {
		this.explicitSave = explicitSave;
	}

	public String getExplicitSaveClass() {
		return explicitSaveClass;
	}
	public void setExplicitSaveClass(String explicitSaveClass) {
		this.explicitSaveClass = explicitSaveClass;
	}
	
}
