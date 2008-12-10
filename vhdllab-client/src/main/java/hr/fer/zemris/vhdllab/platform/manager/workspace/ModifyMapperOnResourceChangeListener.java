package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAdapter;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyMapperOnResourceChangeListener extends FileAdapter implements
        ProjectListener {

    @Autowired
    private DefaultIdentifierToInfoObjectMapper mapper;

    @Override
    public void projectCreated(ProjectInfo project) {
        mapper.addProject(project);
    }

    @Override
    public void projectDeleted(ProjectInfo project) {
    }

    @Override
    public void fileCreated(FileReport report) {
        mapper.addFile(report.getFile());
    }

    @Override
    public void fileDeleted(FileReport report) {
        mapper.removeFile(report.getFile());
    }

}
