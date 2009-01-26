package hr.fer.zemris.vhdllab.platform.gui.dialog.login;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;

import org.springframework.stereotype.Component;

@Component
public class LoginDialogManager extends LocalizationSupport implements
        DialogManager {

    private boolean showRetryMessage = false;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T showDialog(Object... args) {
        LoginDialog dialog = new LoginDialog(this, showRetryMessage);
        showRetryMessage = true;
        return (T) dialog.getResult();
    }

}
