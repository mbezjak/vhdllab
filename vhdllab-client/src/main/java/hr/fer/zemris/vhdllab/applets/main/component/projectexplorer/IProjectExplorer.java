package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

public interface IProjectExplorer extends View {
    
	Caseless getSelectedProject();
	FileIdentifier getSelectedFile();
	
}
