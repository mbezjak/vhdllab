package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table = "GENERALOPTIONS"
 */
public class GeneralOptions {
	
	private Long id;
	private String content;
	
	public GeneralOptions() {}
	
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
	 *  length = "1024"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
