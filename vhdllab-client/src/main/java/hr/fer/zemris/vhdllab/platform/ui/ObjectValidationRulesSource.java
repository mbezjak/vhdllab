package hr.fer.zemris.vhdllab.platform.ui;

import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.project.ProjectFormObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.closure.Constraint;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

public class ObjectValidationRulesSource extends DefaultRulesSource {

    @Autowired
    private WorkspaceManager workspaceManager;

    public ObjectValidationRulesSource() {
        super();
        addRules(createProjectRules());
    }

    private Rules createProjectRules() {
        return new Rules(ProjectFormObject.class) {
            @Override
            protected void initRules() {
                add("projectName", getCommonNameConstraint());
            }
        };
    }

    Constraint getCommonNameConstraint() {
        return all(new Constraint[] { required(), maxLength(255),
                NameFormatConstraint.instance(),
                new ProjectExistsConstraint(workspaceManager) });
    }

}
