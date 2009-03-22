package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import java.util.EventListener;

@AutoPublished(publisher = DefaultWorkspaceManager.class)
public interface WorkspaceListener extends EventListener {

    void fileCreated(FileReport report);

    void fileSaved(FileReport report);

    void fileDeleted(FileReport report);

    void projectCreated(Project project);

    void projectDeleted(Project project);

}
