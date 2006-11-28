package hr.fer.zemris.vhdllab.model;

import java.util.Set;

/**
 * @hibernate.class
 *  table="PROJECTS"
 */
public class Project {
	
	private Long id;
	private String projectName;
	private String ownerID;
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
	 *  length="255"
	 *  not-null="true"
	 */
	public String getOwnerId() {
		return ownerID;
	}
	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	/**
	 * @hibernate.property
	 *  column="NAME"
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
	 *  order-by = "FILE_ID"
	 *  cascade="all"
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

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Project)) return false;
		Project other = (Project) o;
		
		if( this.id != null && other.id != null ) return this.id.equals(other.id);
		else if( this.id == null && other.id == null )
			return this.ownerID.equals(other.ownerID) &&
					this.projectName.equals(other.projectName) &&
					this.files.equals(other.files);
		else return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Project '").append(projectName).append("', id=")
			.append(id).append(", ownerID=").append(ownerID)
			.append(", has ").append(files.size()).append(" files:\n");
		
		for(File f : files) {
			sb.append(f).append("\n");
		}
		
		return sb.toString();
	}
}