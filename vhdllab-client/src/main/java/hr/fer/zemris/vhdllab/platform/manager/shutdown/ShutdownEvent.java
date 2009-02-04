package hr.fer.zemris.vhdllab.platform.manager.shutdown;

public class ShutdownEvent {

    private boolean cancelShutdown = false;

    public void cancelShutdown() {
        cancelShutdown = true;
    }

    public boolean isCancelShutdown() {
        return cancelShutdown;
    }

}
