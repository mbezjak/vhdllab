package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.SimulatableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;

import java.util.List;

import org.apache.commons.collections.Predicate;
import org.springframework.richclient.core.Guarded;

public class WorkspaceHasSimulatableFileCommandGuard extends
        WorkspaceNotEmptyCommandGuard {

    public WorkspaceHasSimulatableFileCommandGuard(WorkspaceManager manager,
            WorkspaceInitializer initializer, Guarded guarded) {
        super(manager, initializer, guarded);
    }

    @Override
    protected void updateEnabledState() {
        Predicate filter = new NotEmptyProjectFilter(manager,
                SimulatableFilesFilter.getInstance());
        List<Project> projects = manager.getProjects(filter, null);
        setEnabled(!projects.isEmpty());
    }

}
