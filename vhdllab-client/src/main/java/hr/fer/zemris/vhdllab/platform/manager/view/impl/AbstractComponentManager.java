package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.view.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewMetadata;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractComponentManager<T extends View> extends
        LocalizationSupport implements ViewManager {

    private static final String TITLE_PREFIX = "title.for.";
    private static final String TOOLTIP_PREFIX = "title.for.";

    // @Autowired
    // private ConfigurableApplicationContext context;
    @Autowired
    private ViewRegistry registry;
    @Resource(name = "groupBasedComponentContainer")
    protected ComponentContainer container;
    protected final ViewIdentifier identifier;
    protected final ComponentGroup group;
    protected T component;

    protected AbstractComponentManager(ViewIdentifier identifier,
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
            component = (T) newInstance();
            // context.getBeanFactory().configureBean(component, );
            component.init();
            configureComponent();
            String title = getTitle();
            String tooltip = getTooltip();
            JPanel panel = component.getPanel();
            container.add(title, tooltip, panel, group);
            registry.add(this, panel);
        }
        select();
    }

    private View newInstance() {
        Class<? extends View> clazz = getComponent(identifier.getMetadata());
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected Class<? extends View> getComponent(ViewMetadata metadata) {
        return metadata.getViewClass();
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
        doClose();
    }

    protected void doClose() {
        if (identifier.getMetadata().isCloseable()) {
            component.dispose();
            JPanel panel = component.getPanel();
            container.remove(panel, group);
            registry.remove(panel);
            // ConfigurableBeanFactory beanFactory = context.getBeanFactory();
            // beanFactory.destroyBean(identifier.getViewName(), component);
            component = null;
        }
    }

    protected void checkIfOpened() {
        if (!isOpened()) {
            throw new NotOpenedException("Component " + identifier
                    + " isn't opened");
        }
    }

    protected String getTitle() {
        return getMessageWithPrefix(TITLE_PREFIX);
    }

    private String getTooltip() {
        return getMessageWithPrefix(TOOLTIP_PREFIX);
    }

    private String getMessageWithPrefix(String prefix) {
        String code = prefix + identifier.getMetadata().getCode();
        return getMessage(code, null);
    }

}
