package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.View;

import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

    private static final long serialVersionUID = 1L;

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void init() {
        doInit();
    }

    @Override
    public void dispose() {
        doDispose();
    }

    protected abstract void doInit();

    protected abstract void doDispose();

}
