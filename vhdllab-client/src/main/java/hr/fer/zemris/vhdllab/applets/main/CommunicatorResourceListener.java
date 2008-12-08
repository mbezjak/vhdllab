package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = Communicator.class)
public interface CommunicatorResourceListener extends EventListener {
    
    void projectCreated(ProjectInfo project);
    
    void fileCreated(ProjectInfo project, FileInfo file, Hierarchy hierarchy);
    
    void fileSaved(ProjectInfo project, FileInfo file, Hierarchy hierarchy);

}
