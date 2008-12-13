package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.component.NotOpenedException;

public abstract class AbstractNoSelectionComponentManager implements
        ComponentManager {

    @Override
    public void open() {
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public void select() throws NotOpenedException {
    }

    @Override
    public boolean isSelected() throws NotOpenedException {
        return false;
    }

    @Override
    public void close() throws NotOpenedException {
    }

}
