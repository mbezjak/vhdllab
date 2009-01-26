package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.platform.manager.view.View;

import java.util.List;

public interface IProjectExplorer extends View {
    
    void dispose();
	
	void addFile(Caseless projectName, Caseless fileName);
	void addProject(Caseless projectName);
	Caseless getSelectedProject();
	FileIdentifier getSelectedFile();
	List<Caseless> getAllProjects();
	List<Caseless> getFilesByProject(Caseless projectName);
	void refreshProject(Caseless projectName);
	void closeProject(Caseless projectName);
	void removeProject(Caseless projectName);
	void removeFile(Caseless projectName, Caseless fileName);
	
}
