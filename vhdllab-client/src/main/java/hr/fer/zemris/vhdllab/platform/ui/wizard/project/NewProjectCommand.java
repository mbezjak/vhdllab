package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingCommand;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.wizard.Wizard;

public class NewProjectCommand extends AbstractResourceCreatingCommand {

    @Override
    protected Form createForm() {
        return new ProjectForm(FormModelHelper
                .createFormModel(new ProjectFormObject()));
    }

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewProjectWizard.class;
    }
}
