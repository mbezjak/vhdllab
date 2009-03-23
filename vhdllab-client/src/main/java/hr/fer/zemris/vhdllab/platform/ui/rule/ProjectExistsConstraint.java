package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.lang.Validate;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;

public class ProjectExistsConstraint extends AbstractPropertyConstraint {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager workspaceManager;

    public ProjectExistsConstraint(WorkspaceManager workspaceManager) {
        Validate.notNull(workspaceManager, "Workspace Manager can't be null");
        this.workspaceManager = workspaceManager;
    }

    @Override
    public boolean test(PropertyAccessStrategy access) {
        Project project = (Project) access.getDomainObject();
        project.setUserId(ApplicationContextHolder.getContext().getUserId());
        return !workspaceManager.exist(project);
    }
}
