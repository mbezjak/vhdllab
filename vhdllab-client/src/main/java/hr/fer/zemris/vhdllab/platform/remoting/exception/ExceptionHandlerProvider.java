package hr.fer.zemris.vhdllab.platform.remoting.exception;

import hr.fer.zemris.vhdllab.platform.gui.dialog.ParametrizedDialogManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandlerProvider implements ExceptionHandler {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(ExceptionHandlerProvider.class);

    @Resource(name = "unknownExceptionDialogManager")
    private ParametrizedDialogManager<Exception, ?> dialogManager;
    private final List<ExceptionHandler> handlers;

    public ExceptionHandlerProvider() {
        this.handlers = new ArrayList<ExceptionHandler>();
    }

    public void addHandler(ExceptionHandler handler) {
        Validate.notNull(handler, "Exception handler can't be null");
        handlers.add(handler);
        LOG.trace("Added ExceptionHandler: " + handler);
    }

    @Override
    public boolean handleException(Exception e) {
        boolean handled = false;
        for (ExceptionHandler h : handlers) {
            handled = h.handleException(e);
            if (handled)
                break;
        }
        if (!handled) {
            LOG.error("Error while executing request to remote server", e);
            dialogManager.showDialog(e);
        }
        return true;
    }

}
