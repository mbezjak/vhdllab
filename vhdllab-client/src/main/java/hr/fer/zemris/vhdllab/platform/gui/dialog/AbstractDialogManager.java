package hr.fer.zemris.vhdllab.platform.gui.dialog;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public abstract class AbstractDialogManager<T> implements DialogManager<T> {

    protected static final String MESSAGE_SUFFIX = ".message";
    protected static final String TITLE_SUFFIX = ".title";

    @Autowired
    protected MessageSource messageSource;

    protected Frame getFrame() {
        return ApplicationContextHolder.getContext().getFrame();
    }

    protected String getMessage() {
        String code = getMessageCode() + MESSAGE_SUFFIX;
        Object[] args = getMessageArguments();
        return messageSource.getMessage(code, args, null);
    }

    protected String getTitle() {
        String code = getMessageCode() + TITLE_SUFFIX;
        Object[] args = getTitleArguments();
        return messageSource.getMessage(code, args, null);
    }

    protected Object[] getMessageArguments() {
        return null;
    }

    protected Object[] getTitleArguments() {
        return null;
    }

    protected int getMessageType() {
        return JOptionPane.INFORMATION_MESSAGE;
    }

    protected abstract String getMessageCode();

}
