package hr.fer.zemris.vhdllab.platform.gui.dialog;

import javax.swing.JOptionPane;

public abstract class AbstractConfirmDialogManager extends
        AbstractOptionPaneDialogManager {

    @Override
    protected Object evaluateOption(int option) {
        return option == JOptionPane.YES_OPTION;
    }

    @Override
    protected int getOptionType() {
        return JOptionPane.YES_NO_OPTION;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.QUESTION_MESSAGE;
    }

}
