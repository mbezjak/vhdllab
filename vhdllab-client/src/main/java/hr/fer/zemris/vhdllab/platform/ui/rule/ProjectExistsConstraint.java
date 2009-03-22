package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.project.ProjectFormObject;

import org.apache.commons.lang.Validate;
import org.springframework.core.closure.support.AbstractConstraint;

public class ProjectExistsConstraint extends AbstractConstraint {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager workspaceManager;

    public ProjectExistsConstraint(WorkspaceManager workspaceManager) {
        Validate.notNull(workspaceManager, "Workspace Manager can't be null");
        this.workspaceManager = workspaceManager;
    }

    @Override
    public boolean test(Object argument) {
        if (argument instanceof String) {
            Project project = ProjectFormObject
                    .asProjectInfo((String) argument);
            return !workspaceManager.exist(project);
        }
        return false;
    }
}
