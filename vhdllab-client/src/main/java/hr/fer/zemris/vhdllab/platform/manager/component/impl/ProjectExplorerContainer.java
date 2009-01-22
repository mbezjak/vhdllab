package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;

import java.util.List;

import javax.swing.JPanel;

@org.springframework.stereotype.Component
class ProjectExplorerContainer extends AbstractComponentContainer implements
        ComponentContainer {

    @Override
    public void add(String title, String tooltip, JPanel component,
            ComponentGroup group) {
        getFrame().setProjectExplorer(component, title);
    }

    @Override
    public void setTitle(String title, JPanel component, ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Can't reset title of project explorer");
    }

    @Override
    public void remove(JPanel component, ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer can't be removed");
    }

    @Override
    public JPanel getSelected(ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer isn't part of tabbed pane");
    }

    @Override
    public void setSelected(JPanel component, ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer isn't part of tabbed pane");
    }

    @Override
    public boolean isSelected(JPanel component, ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer isn't part of tabbed pane");
    }

    @Override
    public List<JPanel> getAll(ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer isn't part of tabbed pane");
    }

    @Override
    public List<JPanel> getAllButSelected(ComponentGroup group) {
        throw new UnsupportedOperationException(
                "Project explorer isn't part of tabbed pane");
    }

}
