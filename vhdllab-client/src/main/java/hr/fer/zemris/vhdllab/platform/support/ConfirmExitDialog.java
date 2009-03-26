package hr.fer.zemris.vhdllab.platform.support;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.stereotype.Component;

@Component
public class ConfirmExitDialog extends ConfirmationDialog {

    protected static final String DEFAULT_YES_COMMAND_ID = "quitCommand";

    private boolean exitConfirmed;

    public boolean isExitConfirmed() {
        return exitConfirmed;
    }

    @Override
    protected void onConfirm() {
        exitConfirmed = true;
    }

    @Override
    protected String getFinishCommandId() {
        return DEFAULT_YES_COMMAND_ID;
    }

    @Override
    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        setConfirmationMessage(getMessage("confirmExitDialog.message"));
    }

}
