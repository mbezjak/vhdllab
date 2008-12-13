package hr.fer.zemris.vhdllab.platform.remoting.exception.dialog;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageShowingDialogManager;
import hr.fer.zemris.vhdllab.platform.gui.dialog.ParametrizedDialogManager;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class UnknownExceptionDialogManager extends
        AbstractMessageShowingDialogManager implements
        ParametrizedDialogManager<Exception, Void> {

    private static final String UNKNOWN_EXCEPTION_MESSAGE = "dialog.unknown.exception";

    @Override
    public Void showDialog(Exception e) {
        String stackTrace;
        if(ApplicationContextHolder.getContext().isApplicationInitialized()) {
            stackTrace = e.getMessage();
        } else {
            stackTrace = ExceptionUtils.getFullStackTrace(e);
        }
        String title = getTitle();
        String text = getMessage() + "\n" + stackTrace;
        int messageType = getMessageType();
        Frame owner = getFrame();
        JOptionPane.showMessageDialog(owner, text, title, messageType);
        return null;
    }

    @Override
    protected String getMessageCode() {
        return UNKNOWN_EXCEPTION_MESSAGE;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}
