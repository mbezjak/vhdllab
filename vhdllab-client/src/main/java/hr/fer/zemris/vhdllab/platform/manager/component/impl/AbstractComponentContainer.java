package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.applets.main.VhdllabFrame;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;

abstract class AbstractComponentContainer implements ComponentContainer {

    protected VhdllabFrame getFrame() {
        return (VhdllabFrame) ApplicationContextHolder.getContext().getFrame();
    }

}
