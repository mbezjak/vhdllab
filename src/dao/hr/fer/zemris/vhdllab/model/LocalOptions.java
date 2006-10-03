package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table = "LOCALOPTIONS"
 */
public class LocalOptions {
	
	private Long id;
	private Long ownerID;
	private String content;
	
	public LocalOptions() {}
	
	/**
	 * @hibernate.id
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
	 *  not-null = "true"
	 *  unique = "true"
	 */
	public Long getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}
	
	/**
	 * @hibernate.property
	 *  not-null = "true"
	 *  length = "1024"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
