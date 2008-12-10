package hr.fer.zemris.vhdllab.platform.listener;

import java.util.EventListener;
import java.util.List;

public interface EventPublisher<T extends EventListener> {

    void addListener(T listener);

    void removeListener(T listener);

    void removeListeners();

    List<T> getListeners();

}
