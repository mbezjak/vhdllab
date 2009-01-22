package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

public class NoSelectionViewManager implements ViewManager {

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
