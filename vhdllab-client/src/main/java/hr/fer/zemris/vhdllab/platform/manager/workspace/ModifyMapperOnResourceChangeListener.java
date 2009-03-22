package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyMapperOnResourceChangeListener implements WorkspaceListener {

    @Autowired
    private DefaultIdentifierToInfoObjectMapper mapper;

    @Override
    public void projectCreated(Project project) {
        mapper.addProject(project);
    }

    @Override
    public void projectDeleted(Project project) {
        mapper.removeProject(project);
    }

    @Override
    public void fileCreated(FileReport report) {
        mapper.addFile(report.getFile());
    }

    @Override
    public void fileSaved(FileReport report) {
        mapper.addFile(report.getFile());
    }

    @Override
    public void fileDeleted(FileReport report) {
        mapper.removeFile(report.getFile());
    }

}
