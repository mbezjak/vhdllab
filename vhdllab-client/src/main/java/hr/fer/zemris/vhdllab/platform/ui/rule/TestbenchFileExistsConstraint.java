package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.lang.Validate;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;

public class TestbenchFileExistsConstraint extends AbstractPropertyConstraint {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager workspaceManager;

    public TestbenchFileExistsConstraint(WorkspaceManager workspaceManager) {
        Validate.notNull(workspaceManager, "Workspace Manager can't be null");
        this.workspaceManager = workspaceManager;
    }

    @Override
    protected boolean test(PropertyAccessStrategy access) {
        File targetFile = (File) access.getPropertyValue("targetFile");
        if (targetFile == null) {
            return true;
        }
        String testbenchName = (String) access.getPropertyValue("testbenchName");
        // File type and data is irrelevant
        File file = new File(testbenchName, FileType.TESTBENCH, "");
        file.setProject(targetFile.getProject());
        return !workspaceManager.exist(file);
    }
}
