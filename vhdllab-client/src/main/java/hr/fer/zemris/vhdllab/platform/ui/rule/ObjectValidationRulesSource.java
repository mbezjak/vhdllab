package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.file.FileFormObject;
import hr.fer.zemris.vhdllab.platform.ui.wizard.project.ProjectFormObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.closure.Constraint;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

public class ObjectValidationRulesSource extends DefaultRulesSource {

    @Autowired
    WorkspaceManager workspaceManager;

    public ObjectValidationRulesSource() {
        super();
        addRules(createProjectRules());
        addRules(createFileRules());
    }

    private Rules createProjectRules() {
        return new Rules(ProjectFormObject.class) {
            @Override
            protected void initRules() {
                add("projectName", new Constraint[] {
                        getCommonNameConstraint(),
                        new ProjectExistsConstraint(workspaceManager) });
            }
        };
    }

    private Rules createFileRules() {
        return new Rules(FileFormObject.class) {
            @Override
            protected void initRules() {
                add("project", required());
                add("fileName", new Constraint[] { getCommonNameConstraint(),
                        new FileExistsConstraint(workspaceManager) });
            }
        };
    }

    Constraint getCommonNameConstraint() {
        return all(new Constraint[] { required(), maxLength(255),
                NameFormatConstraint.instance() });
    }

}
