package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public final class ShutdownManager extends
        AbstractEventPublisher<ShutdownListener> {

    /**
     * An exit status ok indicating normal application termination.
     */
    private static final int EXIT_STATUS_OK = 0;

    @Resource(name = "confirmExitDialogManager")
    private DialogManager dialogManager;

    public ShutdownManager() {
        super(ShutdownListener.class);
    }

    public void shutdownWithConfirmation() {
        if (dialogManager.showDialog()) {
            logger.debug("Shutting down application");
            ShutdownEvent event = new ShutdownEvent();
            fireShutdownInProgress(event);
            if (!event.isCancelShutdown()) {
                System.exit(EXIT_STATUS_OK);
            }
        }
    }

    private void fireShutdownInProgress(ShutdownEvent event) {
        int minLevel = ShutdownListener.MIN_SHUTDOWN_LEVEL;
        int maxLevel = ShutdownListener.MAX_SHUTDOWN_LEVEL;
        for (int shutdownLevel = minLevel; shutdownLevel <= maxLevel; shutdownLevel++) {
            for (ShutdownListener l : getListeners()) {
                if (l.getShutdownLevel() == shutdownLevel) {
                    try {
                        l.shutdownInProgress(event);
                    } catch (RuntimeException e) {
                        logger.debug("Error in shutdown listener", e);
                        // suppress any error in shutdown procedure
                    }
                    if (event.isCancelShutdown()) {
                        return;
                    }
                }
            }
        }
    }

}
