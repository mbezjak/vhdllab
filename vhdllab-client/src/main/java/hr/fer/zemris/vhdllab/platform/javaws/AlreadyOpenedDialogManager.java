package hr.fer.zemris.vhdllab.platform.javaws;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageShowingDialogManager;

import org.springframework.stereotype.Component;

@Component
public class AlreadyOpenedDialogManager extends
        AbstractMessageShowingDialogManager {

    private static final String ALREADY_OPENED_MESSAGE = "dialog.already.opened";

    @Override
    protected String getMessageCode() {
        return ALREADY_OPENED_MESSAGE;
    }

}
