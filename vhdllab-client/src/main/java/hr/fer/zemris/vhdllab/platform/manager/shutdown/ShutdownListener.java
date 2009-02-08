package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;

import java.util.EventListener;

@AutoPublished(publisher = ShutdownManager.class)
public interface ShutdownListener extends EventListener {

    void shutdownWithGUI(ShutdownEvent event);

    void shutdownWithoutGUI();

}
