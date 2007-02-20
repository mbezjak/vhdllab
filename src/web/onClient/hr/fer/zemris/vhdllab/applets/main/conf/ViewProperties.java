package hr.fer.zemris.vhdllab.applets.main.conf;

public class ViewProperties {

	private String name;
	private String viewType;
	private String clazz;
	private boolean singleton;
	
	public ViewProperties() {}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public boolean isSingleton() {
		return singleton;
	}
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
}
