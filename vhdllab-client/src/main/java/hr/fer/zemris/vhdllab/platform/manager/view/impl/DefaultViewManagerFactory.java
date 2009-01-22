package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManagerFactory;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DefaultViewManagerFactory extends
        AbstractComponentManagerFactory<ViewManager> implements
        ViewManagerFactory {

    public DefaultViewManagerFactory() {
        super(ComponentGroup.VIEW);
    }

    @Override
    protected ViewManager newInstance(ViewIdentifier identifier) {
        return new SingleInstanceViewManager(identifier);
    }

    @Override
    protected ViewManager newMulticastInstance(List<ViewManager> managers) {
        return new MulticastViewManager(managers);
    }

    @Override
    protected ViewManager newNoSelectionInstance() {
        return new NoSelectionViewManager();
    }

    @Override
    protected boolean requiredIdentifierType(ViewIdentifier identifier) {
        return identifier.getClass() == ViewIdentifier.class;
    }

}
