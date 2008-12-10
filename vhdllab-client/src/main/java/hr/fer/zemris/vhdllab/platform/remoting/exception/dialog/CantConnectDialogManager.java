package hr.fer.zemris.vhdllab.platform.remoting.exception.dialog;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageShowingDialogManager;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class CantConnectDialogManager extends
        AbstractMessageShowingDialogManager {

    private static final String CANT_CONNECT_MESSAGE = "dialog.cant.connect";

    @Override
    protected String getMessageCode() {
        return CANT_CONNECT_MESSAGE;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}
