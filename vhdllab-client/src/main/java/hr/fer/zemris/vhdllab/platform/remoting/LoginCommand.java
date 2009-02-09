package hr.fer.zemris.vhdllab.platform.remoting;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CompositeDialogPage;
import org.springframework.richclient.dialog.TabbedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;

public class LoginCommand extends ApplicationWindowAwareCommand {

    private UsernamePasswordCredentials credentials;

    public LoginCommand() {
        super("loginCommand");
    }

    public UsernamePasswordCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(UsernamePasswordCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    protected void doExecuteCommand() {
        CompositeDialogPage tabbedPage = new TabbedDialogPage("loginForm");

        final LoginForm loginForm = new LoginForm();

        tabbedPage.addForm(loginForm);

        ApplicationDialog dialog = new TitledPageApplicationDialog(tabbedPage) {
            @Override
            protected boolean onFinish() {
                loginForm.commit();
                UsernamePasswordCredentials authentication = loginForm
                        .getAuthentication();
                setCredentials(authentication);
                return authentication != null;
            }

            @Override
            protected void onCancel() {
                super.onCancel(); // Close the dialog
                System.exit(2);
            }

            @Override
            protected ActionCommand getCallingCommand() {
                return LoginCommand.this;
            }

            @Override
            protected void onAboutToShow() {
                loginForm.requestFocusInWindow();
            }
        };
        dialog.showDialog();
    }

}
