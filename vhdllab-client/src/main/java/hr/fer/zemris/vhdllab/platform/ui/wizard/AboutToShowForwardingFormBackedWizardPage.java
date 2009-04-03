package hr.fer.zemris.vhdllab.platform.ui.wizard;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.FormBackedWizardPage;

public class AboutToShowForwardingFormBackedWizardPage extends
        FormBackedWizardPage {

    public AboutToShowForwardingFormBackedWizardPage(Form backingForm) {
        super(backingForm);
    }

    @Override
    public void onAboutToShow() {
        FormUtils.forwardAboutToShow(getBackingForm());
        super.onAboutToShow();
    }

}
