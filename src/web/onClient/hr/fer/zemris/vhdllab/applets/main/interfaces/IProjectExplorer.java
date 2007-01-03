package hr.fer.zemris.vhdllab.applets.main.interfaces;

import java.util.List;

public interface IProjectExplorer {

	void setProjectContainer(ProjectContainer container);
	void addFile(String projectName, String fileName);
	void addProject(String projectName);
	
	/**
	 * Sets an active project. An active project is a project which user is currently
	 * working on. Note that there is a possibility that user does not have an active project.
	 * Such case is, for example, when user does not have any projects at all. Since that is 
	 * the case, <code>projectName</code> may be null and thereby indicating that there is no
	 * active project.
	 * @param projectName a project name that should be made active
	 * @see #getSelectedProject()
	 */
	void setActiveProject(String projectName);
	
	/**
	 * Returns a current active project. An active project is a project which user is currently
	 * working on. Note that there is a possibility that user does not have an active project.
	 * Such case is, for example, when user does not have any projects at all. If there is no
	 * active project this method will return <code>null</code>.
	 * @return a current active project or <code>null</code> if there is no active project.
	 * @see #setActiveProject(String)
	 */
	String getSelectedProject();
	List<String> getAllProjects();
	List<String> getFilesByProject(String projectName);
	void refreshProject(String projectName);
	void closeProject(String projectName);
	void removeProject(String projectName);
	void removeFile(String projectName, String fileName);
	
}
