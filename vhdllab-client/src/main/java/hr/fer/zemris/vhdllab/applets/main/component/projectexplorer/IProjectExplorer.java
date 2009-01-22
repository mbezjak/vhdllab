package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import java.util.List;

public interface IProjectExplorer {
    
    void init();
    void dispose();
    void setSystemContainer(ISystemContainer container);
    void setEditorManagerFactory(EditorManagerFactory editorManagerFactory);
    
	
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
    void setMapper(IdentifierToInfoObjectMapper mapper);
	
}
