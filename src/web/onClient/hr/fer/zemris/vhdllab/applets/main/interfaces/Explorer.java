package hr.fer.zemris.vhdllab.applets.main.interfaces;

import java.util.List;

public interface Explorer {

	void setProjectContainer(ProjectContainer pContainer);
	void addFile(String projectName, String fileName);
	void addProject(String projectName);
	void setActiveProject(String projectName);
	List<String> getAllProjects();
	List<String> getFilesByProject(String projectName);
	void closeProject(String projectName);
	void removeProject(String projectName);
	void removeFile(String projectName, String fileName);
	
}
