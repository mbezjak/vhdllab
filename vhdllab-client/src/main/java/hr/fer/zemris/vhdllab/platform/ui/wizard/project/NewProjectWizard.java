package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.wizard.AbstractWizard;
import org.springframework.richclient.wizard.FormBackedWizardPage;
import org.springframework.richclient.wizard.WizardDialog;
import org.springframework.stereotype.Component;

@Component
public class NewProjectWizard extends AbstractWizard implements
        ActionCommandExecutor {

    @Autowired
    private ProjectManager projectManager;

    ProjectForm form;

    public NewProjectWizard() {
        super("newProjectWizard");
    }

    @Override
    public void addPages() {
        addPage(new FormBackedWizardPage(form));
    }

    @Override
    protected boolean onFinish() {
        form.commit();
        ProjectFormObject p = (ProjectFormObject) form.getFormObject();
        projectManager.create(ProjectFormObject.asProjectInfo(p
                .getProjectName()));
        return true;
    }

    @Override
    public void execute() {
        form = new ProjectForm(FormModelHelper
                .createFormModel(new ProjectFormObject()));
        WizardDialog dialog = new WizardDialog(this) {
            @Override
            protected void onAboutToShow() {
                super.onAboutToShow();
                form.requestFocusInWindow();
            }
        };
        dialog.showDialog();
    }

}
