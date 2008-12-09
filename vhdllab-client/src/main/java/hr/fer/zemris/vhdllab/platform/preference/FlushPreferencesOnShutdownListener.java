package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownListener;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class FlushPreferencesOnShutdownListener implements ShutdownListener {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(FlushPreferencesOnShutdownListener.class);

    @Override
    public void shutdownInProgress() {
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException e) {
            LOG.error("Error while flushing preferences", e);
        }
    }

}
