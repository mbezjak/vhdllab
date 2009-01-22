package hr.fer.zemris.vhdllab.platform.remoting.exception.dialog;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageDialogManager;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class UnknownExceptionDialogManager extends AbstractMessageDialogManager {

    @Override
    public <T> T showDialog(Object... args) {
        Exception e = (Exception) args[0];
        String stackTrace;
        if (ApplicationContextHolder.getContext().isApplicationInitialized()) {
            stackTrace = e.getMessage();
        } else {
            stackTrace = ExceptionUtils.getFullStackTrace(e);
        }
        String title = getTitle(null);
        String text = getText(null) + "\n" + stackTrace;
        int messageType = getMessageType();
        Frame owner = getFrame();
        JOptionPane.showMessageDialog(owner, text, title, messageType);
        return null;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}
