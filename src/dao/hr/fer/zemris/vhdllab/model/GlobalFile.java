package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table = "GLOBAL_FILES"
 */
public class GlobalFile {
	
	private Long id;
	private String name;
	private String type;
	private String content;
	
	public GlobalFile() {}
	
	/**
	 * @hibernate.id
	 *  column = "GLOBAL_FILE_ID"
	 *  generator-class = "native"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @hibernate.property
	 *  column = "NAME"
	 *  length = "255"
	 *  not-null = "true"
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @hibernate.property
	 *  column = "TYPE"
	 *  not-null = "true"
	 *  length = "255"
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @hibernate.property
	 *  column = "CONTENT"
	 *  type = "text"
	 *  length = "65536"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GlobalFile)) return false;
		GlobalFile other = (GlobalFile) o;
		
		if( this.id != null && other.id != null ) return this.id.equals(other.id);
		else if( this.id == null && other.id == null ) return this.name.equals(other.name);
		else return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Global file '").append(name).append("', id=")
			.append(id).append(", type=").append(type)
			.append(", has content:\n").append(content);
		return sb.toString();
	}
}
