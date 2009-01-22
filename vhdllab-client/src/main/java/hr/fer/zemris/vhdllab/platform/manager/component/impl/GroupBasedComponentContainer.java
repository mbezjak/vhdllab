package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;

import java.util.List;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

@org.springframework.stereotype.Component
public class GroupBasedComponentContainer implements ComponentContainer {

    @Resource(name = "editorContainer")
    private ComponentContainer editorContainer;
    @Resource(name = "viewContainer")
    private ComponentContainer viewContainer;
    @Resource(name = "projectExplorerContainer")
    private ComponentContainer projectExplorerContainer;

    @Override
    public void add(String title, String tooltip, JPanel component,
            ComponentGroup group) {
        Validate.notNull(title, "Title can't be null");
        Validate.notNull(component, "Component can't be null");
        ComponentContainer container = getComponentContainerBasedOn(group);
        container.add(title, tooltip, component, null);
    }

    @Override
    public void setTitle(String title, JPanel component, ComponentGroup group) {
        Validate.notNull(title, "Title can't be null");
        Validate.notNull(component, "Component can't be null");
        ComponentContainer container = getComponentContainerBasedOn(group);
        container.setTitle(title, component, null);
    }

    @Override
    public void remove(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        ComponentContainer container = getComponentContainerBasedOn(group);
        container.remove(component, null);
    }

    @Override
    public JPanel getSelected(ComponentGroup group) {
        ComponentContainer container = getComponentContainerBasedOn(group);
        return container.getSelected(null);
    }

    @Override
    public void setSelected(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        ComponentContainer container = getComponentContainerBasedOn(group);
        container.setSelected(component, null);
    }

    @Override
    public boolean isSelected(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        ComponentContainer container = getComponentContainerBasedOn(group);
        return container.isSelected(component, null);
    }

    @Override
    public List<JPanel> getAll(ComponentGroup group) {
        ComponentContainer container = getComponentContainerBasedOn(group);
        return container.getAll(null);
    }

    @Override
    public List<JPanel> getAllButSelected(ComponentGroup group) {
        ComponentContainer container = getComponentContainerBasedOn(group);
        return container.getAllButSelected(null);
    }

    private ComponentContainer getComponentContainerBasedOn(ComponentGroup group) {
        Validate.notNull(group, "Component group can't be null");
        switch (group) {
        case EDITOR:
            return editorContainer;
        case PROJECT_EXPLORER:
            return projectExplorerContainer;
        case VIEW:
            return viewContainer;
        default:
            throw new IllegalStateException("Unrecognized component group: "
                    + group);
        }
    }

}
