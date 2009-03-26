package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingCommand;
import hr.fer.zemris.vhdllab.platform.util.FormModelUtils;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.Wizard;

public class NewSourceCommand extends AbstractResourceCreatingCommand {

    @Override
    protected Form createForm() {
        return new FileForm(FormModelUtils.createFormModel(new File()));
    }

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSourceWizard.class;
    }

}
