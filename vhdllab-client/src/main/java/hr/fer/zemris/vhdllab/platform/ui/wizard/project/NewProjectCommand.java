package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingCommand;
import hr.fer.zemris.vhdllab.platform.util.FormModelUtils;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.Wizard;

public class NewProjectCommand extends AbstractResourceCreatingCommand {

    @Override
    protected Form createForm() {
        return new ProjectForm(FormModelUtils.createFormModel(new Project()));
    }

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewProjectWizard.class;
    }

}
