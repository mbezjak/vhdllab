package hr.fer.zemris.vhdllab.platform.remoting.exception.dialog;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractMessageShowingDialogManager;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class CertificateExpiredDialogManager extends
        AbstractMessageShowingDialogManager {

    private static final String CERTIFICATE_EXPIRED_MESSAGE = "dialog.certificate.expired";

    @Override
    protected String getMessageCode() {
        return CERTIFICATE_EXPIRED_MESSAGE;
    }

    @Override
    protected int getMessageType() {
        return JOptionPane.ERROR_MESSAGE;
    }

}
