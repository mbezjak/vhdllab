package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.source.FileForm;
import hr.fer.zemris.vhdllab.platform.util.FormModelUtils;

import org.springframework.richclient.form.Form;

public abstract class AbstractFileCreatingCommand extends
        AbstractResourceCreatingCommand {

    @Override
    protected Form createForm() {
        return new FileForm(FormModelUtils.createFormModel(new File()));
    }

}
