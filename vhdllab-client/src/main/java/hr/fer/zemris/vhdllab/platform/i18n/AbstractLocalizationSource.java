package hr.fer.zemris.vhdllab.platform.i18n;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

public abstract class AbstractLocalizationSource implements LocalizationSource {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, null);
    }

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    @Override
    public JFrame getFrame() {
        return Application.instance().getActiveWindow().getControl();
    }

}
