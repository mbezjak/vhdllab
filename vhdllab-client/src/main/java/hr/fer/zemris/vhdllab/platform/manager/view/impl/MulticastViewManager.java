package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

import java.util.List;

public class MulticastViewManager extends
        AbstractMulticastViewManager<ViewManager> implements ViewManager {

    public MulticastViewManager(List<ViewManager> managers) {
        super(managers);
    }

}
