package hr.fer.zemris.vhdllab.platform.manager.shutdown;

public class ShutdownEvent {

    private boolean shutdownCanceled = false;

    public void cancelShutdown() {
        shutdownCanceled = true;
    }

    public boolean isShutdownCanceled() {
        return shutdownCanceled;
    }

}
