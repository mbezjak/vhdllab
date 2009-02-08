package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;

import org.springframework.stereotype.Component;

@Component
public final class ShutdownManager extends
        AbstractEventPublisher<ShutdownListener> {

    public ShutdownManager() {
        super(ShutdownListener.class);
    }

    public boolean shutdownWithGUI() {
        logger.debug("Closing application");
        ShutdownEvent event = new ShutdownEvent();
        fireShutdownWithGUI(event);
        return event.isShutdownCanceled();
    }

    public void shutdownWithoutGUI() {
        logger.debug("Shutting down application");
        fireShutdownWithoutGUI();
    }

    private void fireShutdownWithGUI(ShutdownEvent event) {
        for (ShutdownListener l : getListeners()) {
            l.shutdownWithGUI(event);
            if (event.isShutdownCanceled()) {
                break;
            }
        }
    }

    private void fireShutdownWithoutGUI() {
        for (ShutdownListener l : getListeners()) {
            try {
                l.shutdownWithoutGUI();
            } catch (RuntimeException e) {
                logger.debug("Error in shutdown listener", e);
                // suppress any error in shutdown procedure without GUI
            }
        }
    }

}
