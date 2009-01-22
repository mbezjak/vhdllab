package hr.fer.zemris.vhdllab.platform.remoting.exception.dialog;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageDialogManager;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class CantConnectDialogManager extends AbstractMessageDialogManager {

    @Override
    protected int getMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}
