package hr.fer.zemris.vhdllab.applets.main.interfaces;

public class FileContent {
	private String projectName;
	private String fileName;
	private String content;
	
	public FileContent(String projectName, String fileName, String content) {
		if(projectName == null || fileName == null || content == null) {
			throw new NullPointerException("Project name, file name and content can not be null.");
		}
		this.projectName = projectName;
		this.fileName = fileName;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getProjectName() {
		return projectName;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof FileContent)) return false;
		FileContent other = (FileContent) o;
		
		return this.projectName.equals(other.projectName) &&
				this.fileName.equals(other.fileName) &&
				this.content.equals(other.content);
	}
	
	@Override
	public int hashCode() {
		return this.projectName.hashCode() ^
				this.fileName.hashCode() ^
				this.content.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("File Content for file '").append(fileName)
			.append("' whose project is '").append(projectName)
			.append("' and has content [").append(content).append("]");
		return sb.toString();
	}
}
