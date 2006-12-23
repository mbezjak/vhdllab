package hr.fer.zemris.vhdllab.applets.main.interfaces;

public class FileIdentifier {

	private String projectName;
	private String fileName;
	
	public FileIdentifier(String projectName, String fileName) {
		if(projectName == null) throw new NullPointerException("Project name can not be null.");
		if(fileName == null) throw new NullPointerException("File name can not be null.");
		this.projectName = projectName;
		this.fileName = fileName;
	}

	public String getProjectName() {
		return projectName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof FileIdentifier)) return false;
		FileIdentifier other = (FileIdentifier) obj;
		
		return this.projectName.equals(other.projectName) &&
				this.fileName.equals(other.fileName);
	}
	
	@Override
	public int hashCode() {
		return this.projectName.hashCode() ^ this.fileName.hashCode();
	}
	
	@Override
	public String toString() {
		return fileName + " [" + projectName + "]";
	}
}
