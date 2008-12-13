package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.impl.AbstractMulticastComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

import java.util.List;

public class MulticastViewManager extends
        AbstractMulticastComponentManager<ViewManager> implements ViewManager {

    public MulticastViewManager(List<ViewManager> managers) {
        super(managers);
    }

}
