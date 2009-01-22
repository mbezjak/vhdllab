package hr.fer.zemris.vhdllab.platform.remoting.exception.handler;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.remoting.exception.AbstractExceptionHandler;

import java.security.cert.CertificateExpiredException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class CertificateExpiredExceptionHandler extends
        AbstractExceptionHandler {

    @Resource(name = "certificateExpiredDialogManager")
    private DialogManager dialogManager;

    @Override
    public boolean handleException(Exception e) {
        if (isOfType(e, CertificateExpiredException.class)) {
            dialogManager.showDialog();
            return exitIfNecessary();
        }
        return false;
    }

}
