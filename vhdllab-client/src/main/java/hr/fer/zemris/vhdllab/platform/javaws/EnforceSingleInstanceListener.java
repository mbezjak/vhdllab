package hr.fer.zemris.vhdllab.platform.javaws;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import javax.jnlp.SingleInstanceListener;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

public final class EnforceSingleInstanceListener implements
        SingleInstanceListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(EnforceSingleInstanceListener.class);

    private final DialogManager dialogManager;

    public EnforceSingleInstanceListener(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    @Override
    public void newActivation(String[] args) {
        LOG.debug("Denied start of second client application");
        JFrame frame = Application.instance().getActiveWindow().getControl();
        if (frame != null) {
            frame.toFront();
            dialogManager.showDialog();
        }
    }

}
