package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = ShutdownManager.class)
public interface ShutdownListener extends EventListener {

    public int MIN_SHUTDOWN_LEVEL = 0;

    public int MAX_SHUTDOWN_LEVEL = 1;

    void shutdownInProgress(ShutdownEvent event);

    int getShutdownLevel();

}
