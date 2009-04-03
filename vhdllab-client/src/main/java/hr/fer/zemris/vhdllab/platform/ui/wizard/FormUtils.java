package hr.fer.zemris.vhdllab.platform.ui.wizard;

import org.springframework.richclient.form.Form;

public abstract class FormUtils {

    public static void forwardAboutToShow(Form form) {
        if (form instanceof AboutToShowHook) {
            ((AboutToShowHook) form).onAboutToShow();
        }
    }

}
