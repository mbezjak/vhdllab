package hr.fer.zemris.vhdllab.platform.gui.dialog;

import javax.swing.JOptionPane;

public abstract class AbstractMessageDialogManager extends
        AbstractOptionPaneDialogManager {

    @Override
    protected Object evaluateOption(int option) {
        return null;
    }

    @Override
    protected int getOptionType() {
        return JOptionPane.DEFAULT_OPTION;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.INFORMATION_MESSAGE;
    }

}
