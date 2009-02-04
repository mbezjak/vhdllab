package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

import org.apache.commons.lang.Validate;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class StatusBarAppender extends AppenderSkeleton {

    private StatusBar statusBar;

    public StatusBarAppender(StatusBar statusBar) {
        Validate.notNull(statusBar, "Status Bar can't be null");
        this.statusBar = statusBar;
    }

    @Override
    protected void append(LoggingEvent event) {
        if (!event.getLevel().equals(Level.INFO)) {
            return;
        }
        statusBar.setMessage(event.getMessage().toString());
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
