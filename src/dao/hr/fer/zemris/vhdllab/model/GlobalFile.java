package hr.fer.zemris.vhdllab.model;

import static hr.fer.zemris.vhdllab.utilities.ModelUtil.*;

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
	
	public GlobalFile(GlobalFile file) {
		this.id = file.getId();
		this.name = file.getName();
		this.type = file.getType();
		this.content = file.getContent();
	}
	
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
	 *  unique = "true"
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
	 *  length = "65535"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GlobalFile)) return false;
		GlobalFile other = (GlobalFile) o;
		
		if( this.id != null && other.id != null ) {
			return this.id.equals(other.id);
		}
		else if( this.id == null && other.id == null ) {
			return globalFileNamesAreEqual(this.name, other.name);
		}
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if( id != null ) return id.hashCode();
		else return name.toUpperCase().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Global file '").append(name).append("', id=")
			.append(id).append(", type=").append(type)
			.append(", has content:\n").append(content);
		return sb.toString();
	}
}
