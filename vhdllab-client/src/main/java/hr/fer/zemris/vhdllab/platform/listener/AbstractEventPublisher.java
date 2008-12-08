package hr.fer.zemris.vhdllab.platform.listener;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public abstract class AbstractEventPublisher<T extends EventListener>
        implements EventPublisher<T> {

    /**
     * Logger for this class
     */
    protected final Logger logger = Logger.getLogger(getClass());

    private final EventListenerList events;
    private final Class<T> listenerClass;

    public AbstractEventPublisher(Class<T> listenerClass) {
        Validate.notNull(listenerClass, "Listener class can't be null");
        this.listenerClass = listenerClass;
        events = new EventListenerList();
    }

    @Override
    public void addListener(T listener) {
        Validate.notNull(listener, "Listener can't be null");
        events.add(listenerClass, listener);
        if (logger.isTraceEnabled()) {
            logger.trace("Added " + listenerClass.getSimpleName() + ": "
                    + listener);
        }
    }

    @Override
    public void removeListener(T listener) {
        Validate.notNull(listener, "Listener can't be null");
        events.remove(listenerClass, listener);
        if (logger.isTraceEnabled()) {
            logger.trace("Removed " + listenerClass.getSimpleName() + ": "
                    + listener);
        }
    }

    @Override
    public void removeListeners() {
        for (T l : getListeners()) {
            removeListener(l);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Removed all listeners");
        }
    }

    @Override
    public T[] getListeners() {
        T[] listeners = events.getListeners(listenerClass);
        ArrayUtils.reverse(listeners);
        return listeners;
    }

}
