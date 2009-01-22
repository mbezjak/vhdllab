package hr.fer.zemris.vhdllab.platform.listener;

import java.util.EventListener;

public class StandaloneEventPublisher<T extends EventListener> extends
        AbstractEventPublisher<T> {

    public StandaloneEventPublisher(Class<T> listenerClass) {
        super(listenerClass);
    }

}
