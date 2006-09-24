package hr.fer.zemris.vhdllab.model;

import java.util.Set;

/**
 * @hibernate.class
 *  table="PROJECTS"
 */
public class Project {
	
	private Long id;
	private String projectName;
	private Long ownerID;
	private Set<File> files;
	
	public Project() {}
	
	/**
	 * @hibernate.id
	 *  generator-class="native"
	 *  column="PROJECT_ID"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.property
	 *  column="OWNER_ID"
	 *  not-null="true"
	 */
	public Long getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * @hibernate.property
	 *  column="PROJECTNAME"
	 *  length="255"
	 *  not-null="true"
	 */
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * @hibernate.set
	 * 	inverse="true"
	 *  lazy="false"
	 * @hibernate.key
	 *  column="PROJECT_ID"
	 * @hibernate.one-to-many
	 *  class="hr.fer.zemris.vhdllab.model.File"
	 */
	public Set<File> getFiles() {
		return files;
	}
	public void setFiles(Set<File> files) {
		this.files = files;
	}
}