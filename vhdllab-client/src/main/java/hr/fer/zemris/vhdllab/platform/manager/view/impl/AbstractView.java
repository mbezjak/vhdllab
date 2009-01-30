package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.view.View;

import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

    private static final long serialVersionUID = 1L;

    protected PlatformContainer container;

    @Override
    public PlatformContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(PlatformContainer container) {
        this.container = container;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}
