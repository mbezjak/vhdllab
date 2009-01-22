package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.View;

public abstract class AbstractViewMetadata extends AbstractComponentMetadata {

    private final Class<? extends View> viewClass;

    public AbstractViewMetadata(Class<? extends View> viewClass) {
        super(viewClass);
        this.viewClass = viewClass;
    }

    @Override
    public Class<? extends View> getViewClass() {
        return viewClass;
    }

}
