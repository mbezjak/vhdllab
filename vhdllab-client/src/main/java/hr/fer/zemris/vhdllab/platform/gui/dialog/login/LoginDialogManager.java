package hr.fer.zemris.vhdllab.platform.gui.dialog.login;

import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import java.awt.Frame;

import org.springframework.stereotype.Component;

@Component
public class LoginDialogManager implements DialogManager<UserCredential> {

    private boolean showRetryMessage = false;

    @Override
    public UserCredential showDialog() {
        UserCredential uc = showLoginDialog(showRetryMessage);
        showRetryMessage = true;
        return uc;
    }

    /**
     * @param displayRetryMessage
     *            a flag indicating if login dialog should show retry message
     *            (not a default message when it says that authentication is
     *            required but a message saying that he should try again because
     *            previous login attempt failed)
     */
    private UserCredential showLoginDialog(boolean displayRetryMessage) {
        Frame owner = ApplicationContextHolder.getContext().getFrame();
        String message;
        if (displayRetryMessage) {
            String name = LoginDialog.BUNDLE_NAME;
            String key = LoginDialog.RETRY_MESSAGE;
            message = ResourceBundleProvider.getBundle(name).getString(key);
        } else {
            message = null;
        }
        LoginDialog dialog = new LoginDialog(owner, message);
        dialog.setVisible(true); // controls are locked here
        int option = dialog.getOption();
        if (option == LoginDialog.OK_OPTION) {
            return dialog.getCredentials();
        }
        return null;
    }

}
