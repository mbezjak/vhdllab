package hr.fer.zemris.vhdllab.platform.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.applets.main.CommunicatorResourceListener;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyMapperOnResourceChangeListener implements
        CommunicatorResourceListener {

    @Autowired
    private DefaultIdentifierToInfoObjectMapper mapper;

    @Override
    public void projectCreated(ProjectInfo project) {
        mapper.addProject(project);
    }

    @Override
    public void fileCreated(ProjectInfo project, FileInfo file,
            Hierarchy hierarchy) {
        mapper.addFile(project, file);
    }

    @Override
    public void fileSaved(ProjectInfo project, FileInfo file,
            Hierarchy hierarchy) {
    }

}
