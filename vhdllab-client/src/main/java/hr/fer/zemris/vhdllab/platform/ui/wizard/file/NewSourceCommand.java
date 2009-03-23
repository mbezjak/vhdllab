package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingCommand;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.wizard.Wizard;

public class NewSourceCommand extends AbstractResourceCreatingCommand {

    @Override
    protected Form createForm() {
        return new FileForm(FormModelHelper.createFormModel(new File()));
    }

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSourceWizard.class;
    }

}
