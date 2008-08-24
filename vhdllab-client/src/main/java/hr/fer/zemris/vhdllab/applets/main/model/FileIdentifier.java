package hr.fer.zemris.vhdllab.applets.main.model;

/**
 * This class uniquely identifies either a project or a file.
 * @author Miro Bezjak
 */
public final class FileIdentifier {

	/** A name of a project */
	private String projectName;
	/** A name of a file */
	private String fileName;
	
	/**
	 * Constructor for this class. File name can be <code>null</code> and if so
	 * then this class will represent a project instead of a file.
	 * @param projectName a name of a project
	 * @param fileName a name of a file
	 * @throws NullPointerException if <code>projectName</code> is <code>null</code>
	 */
	public FileIdentifier(String projectName, String fileName) {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		this.projectName = projectName;
		this.fileName = fileName;
	}
	
	/**
	 * Constructor for this class. File name will be set to <code>null</code>.
	 * This class represents a project instead of a file when file name is
	 * <code>null</code>.
	 * @param projectName a name of a project
	 * @throws NullPointerException if <code>projectName</code> is <code>null</code>
	 */
	public FileIdentifier(String projectName) {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		this.projectName = projectName;
		this.fileName = null;
	}

	/**
	 * Getter for project name.
	 * @return a name of a project
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Getter for file name. This method might return <code>null</code>, if so
	 * then this <code>FileIdentifier</code> represents a project.
	 * @return a file name of this FileIdentifier
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Return <code>true</code> if this <code>FileIdentifier</code> represents
	 * a file, <code>false</code> otherwise.
	 * @return <code>true</code> if this <code>FileIdentifier</code> represents
	 * 		a file, <code>false</code> otherwise
	 */
	public boolean isFile() {
		return !this.isProject();
	}
	
	/**
	 * Return <code>true</code> if this <code>FileIdentifier</code> represents
	 * a project, <code>false</code> otherwise.
	 * @return <code>true</code> if this <code>FileIdentifier</code> represents
	 * 		a project, <code>false</code> otherwise
	 */
	public boolean isProject() {
		return this.fileName == null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof FileIdentifier)) return false;
		FileIdentifier other = (FileIdentifier) obj;
		
		if(!this.projectName.equals(other.projectName)) return false;
		if(this.isProject() && other.isProject()) return true;
		else if(this.isFile() && other.isFile()) {
			return this.fileName.equalsIgnoreCase(other.fileName);
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = this.projectName.hashCode();
		if(this.isFile()) {
			hash ^= this.fileName.toUpperCase().hashCode();
		}
		return hash;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(this.isProject()) {
			return "Project '" + projectName + "'";
		} else {
			return fileName + " [" + projectName + "]";
		}
	}
}
