package hr.fer.zemris.vhdllab.platform.ui.wizard;

import org.apache.commons.lang.Validate;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.AbstractWizard;
import org.springframework.richclient.wizard.FormBackedWizardPage;

public abstract class FormBackedWizard extends AbstractWizard {

    private Form form;

    public FormBackedWizard(String wizardId) {
        super(wizardId);
    }

    public void setForm(Form form) {
        Validate.notNull(form, "Form can't be null");
        this.form = form;
    }

    @Override
    public void addPages() {
        addPage(new FormBackedWizardPage(form));
    }

    @Override
    protected boolean onFinish() {
        form.commit();
        onWizardFinished(form.getFormObject());
        return true;
    }

    protected abstract void onWizardFinished(Object formObject);

}
