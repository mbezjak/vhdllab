package hr.fer.zemris.vhdllab.applets.main.model;

/**
 * Represents a one file and its content.
 * @author Miro Bezjak
 */
public final class FileContent {
	
	/** A name of a project */
	private String projectName;
	/** A name of a file */
	private String fileName;
	/** A content of a file that this class represents */
	private String content;
	
	/**
	 * Constructor.
	 * @param projectName a name of a project
	 * @param fileName a name of a file
	 * @param content a content of a file that this class represents
	 * @throws NullPointerException if either <code>projectName</code>,
	 * 		<code>fileName</code> or content is <code>null</code>
	 */
	public FileContent(String projectName, String fileName, String content) {
		if(projectName == null || fileName == null || content == null) {
			throw new NullPointerException("Project name, file name or content can not be null.");
		}
		this.projectName = projectName;
		this.fileName = fileName;
		this.content = content;
	}
	 
	/**
	 * Getter for content.
	 * @return a content of this <code>FileContent</code>
	 */
	public String getContent() {
		return content;
	}
	/**
	 * Setter for content
	 * @param content a conent to set this <code>FileContent</code>
	 * @throws NullPointerException is <code>content</code> is <code>null</code>
	 */
	public void setContent(String content) {
		if(content == null) {
			throw new NullPointerException("Content can not be null.");
		}
		this.content = content;
	}
	
	/**
	 * Getter for file name.
	 * @return a name of a file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Getter for project name.
	 * @return a name of a project
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof FileContent)) return false;
		FileContent other = (FileContent) o;
		
		return this.projectName.equals(other.projectName) &&
				this.fileName.equalsIgnoreCase(other.fileName) &&
				this.content.equals(other.content);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.projectName.hashCode() ^
				this.fileName.toUpperCase().hashCode() ^
				this.content.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File Content for file '").append(fileName)
			.append("' whose project is '").append(projectName)
			.append("' and has content [").append(content).append("]");
		return sb.toString();
	}
}
