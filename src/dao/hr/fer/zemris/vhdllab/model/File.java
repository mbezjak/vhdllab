package hr.fer.zemris.vhdllab.model;

/**
 * @hibernate.class
 *  table="FILES"
 */
public class File implements Comparable<File> {
	
	private Long id;
	private String fileName;
	private String fileType;
	private String content;
	private Project project;
	
	public File() {}
	
	/**
	 * @hibernate.id
	 * 	generator-class="native"
	 *  column="FILE_ID"
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @hibernate.property
	 * 	column="CONTENT"
	 *  type = "text"
	 *  length="65536"
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @hibernate.property
	 * 	column="NAME"
	 *  length="255"
	 *  not-null="true"
	 */
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @hibernate.property
	 * 	column="TYPE"
	 *  length="255"
	 * 	not-null="true"
	 */
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	/**
	 * @hibernate.many-to-one
	 *  column="PROJECT_ID"
	 *  cascade="none"
	 *  not-null="true"
	 */
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	@Override
	public int hashCode() {
		if( id != null ) return id.hashCode();
		else return fileName.toUpperCase().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof File)) return false;
		File other = (File) o;

		if( this.id != null && other.id != null ) return this.id.equals(other.id);
		else if( this.id == null && other.id == null ) return this.fileName.equalsIgnoreCase(other.fileName);
		else return false;
	}
	
	public int compareTo(File other) {
		long val = 0;
		
		/* File name ordering */
		if( this.fileName != null && other.fileName != null ) val = this.fileName.compareToIgnoreCase(other.fileName);
		else if ( this.fileName != null && other.fileName == null ) val = 1;
		else if ( this.fileName == null && other.fileName != null ) val = -1;
		else val = 0;

		if( val != 0) return (int)val;
		
		/* File type ordering if files have the same name */
		if( this.fileType != null && other.fileType != null ) val = this.fileType.compareTo(other.fileType);
		else if ( this.fileType != null && other.fileType == null ) val = 1;
		else if ( this.fileType == null && other.fileType != null ) val = -1;
		else val = 0;
		
		if( val != 0) return (int)val;
		
		/* File ID ordering if files have the same name and type */
		if( this.id != null && other.id != null ) val = this.id.longValue() - other.id.longValue();
		else if ( this.id != null && other.id == null ) val = 1;
		else if ( this.id == null && other.id != null ) val = -1;
		else val = 0;
		
		if(val>0) return 1;
		else if(val<0) return -1;
		else return 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File '").append(fileName).append("', id=")
			.append(id).append(", filetype=").append(fileType)
			.append(", belongs to Project '").append(project.getProjectName())
			.append("'(").append(project.getId()).append("), has content:\n")
			.append(content);
		return sb.toString();
	}
}