package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.component.IComponent;
import hr.fer.zemris.vhdllab.platform.manager.component.NotOpenedException;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;

public abstract class AbstractComponentManager<T extends IComponent> implements
        ComponentManager {

    private static final String TITLE_PREFIX = "title.for.";
    private static final String TOOLTIP_PREFIX = "title.for.";

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ComponentRegistry registry;
    @Autowired
    private ComponentContainer container;
    private final ComponentIdentifier identifier;
    private final ComponentGroup group;
    protected T component;

    protected AbstractComponentManager(ComponentIdentifier identifier,
            ComponentGroup group) {
        Validate.notNull(identifier, "Component identifier can't be null");
        Validate.notNull(group, "Component group can't be null");
        this.identifier = identifier;
        this.group = group;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void open() {
        if (!isOpened()) {
            component = (T) context.getBean(identifier.getComponentName());
            configureComponent();
            String title = getTitle();
            String tooltip = getTooltip();
            JPanel panel = component.getPanel();
            container.add(title, tooltip, panel, group);
            registry.add(this, panel);
        }
        select();
    }

    protected abstract void configureComponent();

    @Override
    public boolean isOpened() {
        return component != null;
    }

    @Override
    public void select() throws NotOpenedException {
        checkIfOpened();
        if (isSelected())
            return;
        container.setSelected(component.getPanel(), group);
    }

    @Override
    public boolean isSelected() throws NotOpenedException {
        checkIfOpened();
        return container.isSelected(component.getPanel(), group);
    }

    @Override
    public void close() throws NotOpenedException {
        checkIfOpened();
        if (isCloseable()) {
            JPanel panel = component.getPanel();
            container.remove(panel, group);
            registry.remove(panel);
            ConfigurableBeanFactory beanFactory = context.getBeanFactory();
            beanFactory.destroyBean(identifier.getComponentName(), component);
            component = null;
        }
    }

    protected boolean isCloseable() {
        return true;
    }

    protected void checkIfOpened() {
        if (!isOpened()) {
            throw new NotOpenedException("Editor " + identifier
                    + " isn't opened");
        }
    }

    private String getTitle() {
        return getMessageWithPrefix(TITLE_PREFIX);
    }

    private String getTooltip() {
        return getMessageWithPrefix(TOOLTIP_PREFIX);
    }

    private String getMessageWithPrefix(String prefix) {
        String code = prefix + identifier.getComponentName();
        return messageSource.getMessage(code, null, null);
    }

}
