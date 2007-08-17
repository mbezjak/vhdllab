package hr.fer.zemris.vhdllab.applets.main.conf;

public class ViewProperties {

	private String id;
	private String clazz;
	private boolean singleton;

	public ViewProperties() {
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
		sb.append("view id=").append(id).append(", class=").append(clazz)
				.append(", singleton=").append(singleton);
		return sb.toString();
	}

}
