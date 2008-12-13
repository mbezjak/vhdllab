package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.View;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements View {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    @Override
    public void init() {
        doInit();
    }

    @PreDestroy
    @Override
    public void dispose() {
        doDispose();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    protected abstract void doInit();

    protected abstract void doDispose();

}
