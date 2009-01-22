package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

public class SingleInstanceViewManager extends AbstractComponentManager<View>
        implements ViewManager {

    public SingleInstanceViewManager(ViewIdentifier identifier) {
        super(identifier, ComponentGroup.VIEW);
    }

    @Override
    protected void configureComponent() {
    }

}
