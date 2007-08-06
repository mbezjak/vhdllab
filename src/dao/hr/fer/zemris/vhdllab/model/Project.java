package hr.fer.zemris.vhdllab.model;

import static hr.fer.zemris.vhdllab.utilities.ModelUtil.projectNamesAreEqual;
import static hr.fer.zemris.vhdllab.utilities.ModelUtil.userIdAreEqual;

import java.util.Set;

/**
 * @hibernate.class
 *  table="PROJECTS"
 */
public class Project {

	private Long id;
	private String projectName;
	private String ownerId;
	private Set<File> files;

	public Project() {}
	
	public Project(Project p) {
		this.id = p.getId();
		this.projectName = p.getProjectName();
		this.ownerId = p.getOwnerId();
		this.files = p.getFiles();
	}

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
		return ownerId;
	}
	public void setOwnerId(String ownerID) {
		this.ownerId = ownerID;
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
	 *  cascade="all-delete-orphan"
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

	public void addFile(File f) {
		if(f == null) throw new NullPointerException("File can not be null.");
		if(f.getProject() != null) {
			f.getProject().removeFile(f);
		}
		f.setProject(this);
		files.add(f);
	}

	public void removeFile(File f) {
		if(f == null) throw new NullPointerException("File can not be null.");
		files.remove(f);
		f.setProject(null);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Project)) return false;
		Project other = (Project) o;

		if( this.id != null && other.id != null ) {
			return this.id.equals(other.id);
		}
		else if( this.id == null && other.id == null ) {
			return userIdAreEqual(this.ownerId, other.ownerId) &&
					projectNamesAreEqual(this.projectName, other.projectName);
		}
		else return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if( id != null ) return id.hashCode();
		else return ownerId.toUpperCase().hashCode() ^
					projectName.toUpperCase().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Project '").append(projectName).append("', id=")
		.append(id).append(", ownerId=").append(ownerId)
		.append(", has ").append(files.size()).append(" files:\n");

		for(File f : files) {
			sb.append(f).append("\n");
		}

		return sb.toString();
	}
}