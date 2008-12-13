package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

public abstract class AbstractMulticastComponentManager<T extends ComponentManager>
        implements ComponentManager {

    protected final List<T> managers;

    public AbstractMulticastComponentManager(List<T> managers) {
        Validate.notNull(managers, "Editor managers can't be null");
        this.managers = new ArrayList<T>(managers);
        for (T man : this.managers) {
            if (!man.isOpened()) {
                throw new IllegalArgumentException(
                        "All components must be opened");
            }
        }
    }

    @Override
    public void open() {
    }

    @Override
    public boolean isOpened() {
        return true;
    }

    @Override
    public void select() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public void close() {
        for (T man : managers) {
            man.close();
        }
    }

}