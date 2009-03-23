package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.lang.Validate;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;

public class FileExistsConstraint extends AbstractPropertyConstraint {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager workspaceManager;

    public FileExistsConstraint(WorkspaceManager workspaceManager) {
        Validate.notNull(workspaceManager, "Workspace Manager can't be null");
        this.workspaceManager = workspaceManager;
    }

    @Override
    protected boolean test(PropertyAccessStrategy access) {
        Project project = (Project) access.getPropertyValue("project");
        String fileName = (String) access.getPropertyValue("fileName");
        // File type and data is irrelevant
        File file = new File(fileName, FileType.SOURCE, "");
        file.setProject(project);
        return !workspaceManager.exist(file);
    }
}
