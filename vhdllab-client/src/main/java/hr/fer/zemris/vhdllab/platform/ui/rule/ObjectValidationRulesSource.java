package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

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
        return new Rules(Project.class) {
            @Override
            protected void initRules() {
                add("name", new Constraint[] { new NameFormatConstraint(),
                        new ProjectExistsConstraint(workspaceManager) });
            }
        };
    }

    private Rules createFileRules() {
        return new Rules(File.class) {
            @Override
            protected void initRules() {
                add("name", new Constraint[] { new NameFormatConstraint(),
                        new FileExistsConstraint(workspaceManager) });
            }
        };
    }

}
