package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManagerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractComponentManagerFactory<T extends ComponentManager>
        implements ComponentManagerFactory<T> {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private ComponentContainer container;
    @Autowired
    private ComponentRegistry registry;
    private final ComponentGroup group;

    public AbstractComponentManagerFactory(ComponentGroup group) {
        Validate.notNull(group, "Component group can't be null");
        this.group = group;
    }

    @Override
    public T get(ComponentIdentifier identifier) {
        Validate.notNull(identifier, "Component identifier can't be null");
        if (!requiredIdentifierType(identifier)) {
            throw new IllegalArgumentException(
                    "Identifier type isn't acceptable: "
                            + identifier.getClass());
        }
        return configureManager(newInstance(identifier));
    }

    @Override
    public T getSelected() {
        return get(container.getSelected(group));
    }

    @Override
    public T getAll() {
        return createManager(container.getAll(group));
    }

    @Override
    public T getAllButSelected() {
        return createManager(container.getAllButSelected(group));
    }

    protected abstract boolean requiredIdentifierType(
            ComponentIdentifier identifier);

    protected abstract T newInstance(ComponentIdentifier identifier);

    protected abstract T newMulticastInstance(List<T> managers);

    protected abstract T newNoSelectionInstance();

    @SuppressWarnings("unchecked")
    protected T configureManager(ComponentManager manager) {
        String beanName = StringUtils.uncapitalize(manager.getClass()
                .getSimpleName());
        context.getBeanFactory().configureBean(manager, beanName);
        return (T) manager;
    }

    @SuppressWarnings("unchecked")
    private T get(JPanel panel) {
        if (panel == null) {
            return newNoSelectionInstance();
        }
        return (T) registry.get(panel);
    }

    private T createManager(List<JPanel> components) {
        List<T> managers = new ArrayList<T>(components.size());
        for (JPanel panel : components) {
            managers.add(get(panel));
        }
        return newMulticastInstance(managers);
    }

}
