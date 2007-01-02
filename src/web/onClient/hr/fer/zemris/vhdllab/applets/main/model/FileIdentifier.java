package hr.fer.zemris.vhdllab.applets.main.model;

public class FileIdentifier {

	private String projectName;
	private String fileName;
	
	public FileIdentifier(String projectName, String fileName) {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		this.projectName = projectName;
		this.fileName = fileName;
	}

	public String getProjectName() {
		return projectName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public boolean isFile() {
		return !this.isProject();
	}
	
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
			hash ^= this.fileName.hashCode();
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
