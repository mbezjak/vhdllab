package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingWizard;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewProjectWizard extends AbstractResourceCreatingWizard {

    @Autowired
    private ProjectManager projectManager;

    public NewProjectWizard() {
        super(BeanUtil.getBeanName(NewProjectWizard.class));
    }

    @Override
    protected void onWizardFinished(Object formObject) {
        ProjectFormObject p = (ProjectFormObject) formObject;
        projectManager.create(ProjectFormObject.asProjectInfo(p
                .getProjectName()));
    }

}
