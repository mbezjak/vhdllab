package hr.fer.zemris.vhdllab.applets.main.model;

/**
 * Represents a one file and its content.
 * 
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
	 * 
	 * @param content
	 *            a content of a file
	 * @throws NullPointerException
	 *             if <code>content</code> is <code>null</code>
	 */
	public FileContent(String content) {
		this(null, null, content);
	}

	/**
	 * Constructor.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @param content
	 *            a content of a file that this class represents
	 * @throws NullPointerException
	 *             if <code>content</code> is <code>null</code>
	 */
	public FileContent(String projectName, String fileName, String content) {
		if (content == null) {
			throw new NullPointerException("Content can not be null.");
		}
		this.projectName = projectName;
		this.fileName = fileName;
		this.content = content;
	}

	/**
	 * Getter for content.
	 * 
	 * @return a content of this <code>FileContent</code>
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter for content.
	 * 
	 * @param content
	 *            a conent to set this <code>FileContent</code>
	 * @throws NullPointerException
	 *             is <code>content</code> is <code>null</code>
	 */
	public void setContent(String content) {
		if (content == null) {
			throw new NullPointerException("Content can not be null.");
		}
		this.content = content;
	}

	/**
	 * Getter for file name.
	 * 
	 * @return a name of a file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Getter for project name.
	 * 
	 * @return a name of a project
	 */
	public String getProjectName() {
		return projectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FileContent other = (FileContent) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (!content.equals(other.content))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + content.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File Content for file '").append(fileName).append(
				"' whose project is '").append(projectName).append(
				"' and has content [").append(content).append("]");
		return sb.toString();
	}
}
