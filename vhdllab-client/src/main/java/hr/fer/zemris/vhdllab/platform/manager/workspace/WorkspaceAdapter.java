package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

public abstract class WorkspaceAdapter implements WorkspaceListener {

    @Override
    public void fileCreated(FileReport report) {
    }

    @Override
    public void fileDeleted(FileReport report) {
    }

    @Override
    public void fileSaved(FileReport report) {
    }

    @Override
    public void projectCreated(Project project) {
    }

    @Override
    public void projectDeleted(Project project) {
    }

}
