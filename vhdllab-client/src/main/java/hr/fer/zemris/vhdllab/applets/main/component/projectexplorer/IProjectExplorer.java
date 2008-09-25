package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.List;

public interface IProjectExplorer {
	
	void addFile(Caseless projectName, Caseless fileName);
	void addProject(Caseless projectName);
	
	/**
	 * Sets an active project. An active project is a project which user is currently
	 * working on. Note that there is a possibility that user does not have an active project.
	 * Such case is, for example, when user does not have any projects at all. Since that is 
	 * the case, <code>projectName</code> may be null and thereby indicating that there is no
	 * active project.
	 * @param projectName a project name that should be made active
	 * @see #getSelectedProject()
	 */
	
	/**
	 * Returns a current active project. An active project is a project which user is currently
	 * working on. Note that there is a possibility that user does not have an active project.
	 * Such case is, for example, when user does not have any projects at all. If there is no
	 * active project this method will return <code>null</code>.
	 * @return a current active project or <code>null</code> if there is no active project.
	 */
	Caseless getSelectedProject();
	FileIdentifier getSelectedFile();
	List<Caseless> getAllProjects();
	List<Caseless> getFilesByProject(Caseless projectName);
	void refreshProject(Caseless projectName);
	void closeProject(Caseless projectName);
	void removeProject(Caseless projectName);
	void removeFile(Caseless projectName, Caseless fileName);
	
}
