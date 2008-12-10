package hr.fer.zemris.vhdllab.platform.gui.dialog;

import java.awt.Frame;

import javax.swing.JOptionPane;

public abstract class AbstractMessageShowingDialogManager extends
        AbstractDialogManager<Void> {

    @Override
    public Void showDialog() {
        String title = getTitle();
        String text = getMessage();
        int messageType = getMessageType();
        Frame owner = getFrame();
        JOptionPane.showMessageDialog(owner, text, title, messageType);
        return null;
    }

}
