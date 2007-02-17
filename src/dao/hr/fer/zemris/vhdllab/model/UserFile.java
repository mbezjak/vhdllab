package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table = "USER_FILES"
 */
public class UserFile {
	
	private Long id;
	private String ownerID;
	private String name;
	private String type;
	private String content;
	
	public UserFile() {}
	
	public UserFile(UserFile file) {
		this.id = file.getId();
		this.ownerID = file.getOwnerID();
		this.name = file.getName();
		this.type = file.getType();
		this.content = file.getContent();
	}
	
	/**
	 * @hibernate.id
	 *  column = "USER_FILE_ID"
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
	 *  column = "OWNER_ID"
	 *  length = "255"
	 *  not-null = "true"
	 */
	public String getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
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
	 *  length = "255"
	 *  not-null = "true"
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
		if(!(o instanceof UserFile)) return false;
		UserFile other = (UserFile) o;
		
		if( this.id != null && other.id != null ) {
			return this.id.equals(other.id);
		}
		else if( this.id == null && other.id == null ) {
			return this.name.equalsIgnoreCase(other.name);
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
		sb.append("User file with ownerID = '").append(ownerID)
			.append("', id=").append(id)
			.append(", type=").append(type)
			.append(", has content:\n").append(content);
		return sb.toString();
	}
}
