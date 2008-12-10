package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractDialogManager;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class ExitApplicationDialogManager extends
        AbstractDialogManager<Boolean> {

    private static final String CONFIRM_EXIT_MESSAGE = "dialog.confirm.exit";

    @Override
    public Boolean showDialog() {
        String title = getTitle();
        String text = getMessage();
        int messageType = getMessageType();
        Frame owner = getFrame();
        int optionType = JOptionPane.YES_NO_OPTION;
        int option = JOptionPane.showConfirmDialog(owner, text, title,
                optionType, messageType);
        return Boolean.valueOf(option == JOptionPane.YES_OPTION);
    }

    @Override
    protected String getMessageCode() {
        return CONFIRM_EXIT_MESSAGE;
    }

}
