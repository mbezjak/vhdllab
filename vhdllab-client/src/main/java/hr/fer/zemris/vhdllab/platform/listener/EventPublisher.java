package hr.fer.zemris.vhdllab.platform.listener;

import java.util.EventListener;

public interface EventPublisher<T extends EventListener> {

    void addListener(T listener);

    void removeListener(T listener);

    void removeListeners();

    T[] getListeners();

}
