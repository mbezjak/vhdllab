package hr.fer.zemris.vhdllab.platform.javaws;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import java.awt.Frame;

import javax.jnlp.SingleInstanceListener;

import org.apache.log4j.Logger;

public final class EnforceSingleInstanceListener implements
        SingleInstanceListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(EnforceSingleInstanceListener.class);

    private final DialogManager<?> dialogManager;

    public EnforceSingleInstanceListener(DialogManager<?> dialogManager) {
        this.dialogManager = dialogManager;
    }

    @Override
    public void newActivation(String[] args) {
        LOG.debug("Denied start of second client application");
        Frame frame = ApplicationContextHolder.getContext().getFrame();
        if (frame != null) {
            frame.toFront();
            dialogManager.showDialog();
        }
    }

}
