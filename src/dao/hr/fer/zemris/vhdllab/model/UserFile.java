package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table = "USER_FILES"
 */
public class UserFile {
	public static final String UFT_THEME = "theme";
	public static final String UFT_APPLET = "applet";
	
	private Long id;
	private Long ownerID;
	private String type;
	private String content;
	
	public UserFile() {}
	
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
	 *  not-null = "true"
	 */
	public Long getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}
	
	/**
	 * @hibernate.property
	 *  column = "TYPE"
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
	 *  not-null = "true"
	 *  length = "1024"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof UserFile)) return false;
		UserFile other = (UserFile) o;
		
		if( this.id != null && other.id != null ) return this.id.equals(other.id);
		else if( this.id == null && other.id == null ) return this.content.equals(other.content);
		else return false;
	}
	
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