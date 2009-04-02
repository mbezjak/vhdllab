package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractFormSupportingWizard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewProjectWizard extends AbstractFormSupportingWizard {

    @Autowired
    private WorkspaceManager workspaceManager;

    private ProjectForm projectForm;

    @Override
    public void addPages() {
        projectForm = new ProjectForm();
        addForm(projectForm);
    }

    @Override
    protected void onWizardFinished() {
        Project project = (Project) projectForm.getFormObject();
        project.setUserId(ApplicationContextHolder.getContext().getUserId());
        workspaceManager.create(project);
    }

}
