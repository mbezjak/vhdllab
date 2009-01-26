package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEditorManager extends LocalizationSupport
        implements EditorManager {

    private static final String TITLE_PREFIX = "title.for.";
    private static final String TOOLTIP_PREFIX = "title.for.";

    // @Autowired
    // private ConfigurableApplicationContext context;
    @Autowired
    protected EditorRegistry registry;
    @Resource(name = "groupBasedComponentContainer")
    protected ComponentContainer container;
    protected final EditorIdentifier identifier;
    protected final ComponentGroup group;
    protected Editor editor;

    @Autowired
    private ISystemContainer systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    protected IdentifierToInfoObjectMapper mapper;

    protected AbstractEditorManager(EditorIdentifier identifier,
            ComponentGroup group) {
        Validate.notNull(identifier, "Editor identifier can't be null");
        Validate.notNull(group, "Component group can't be null");
        this.identifier = identifier;
        this.group = group;
    }

    @Override
    public void open() {
        if (!isOpened()) {
            editor = newInstance();
            editor.setSystemContainer(systemContainer);
            editor.setEditorManagerFactory(editorManagerFactory);
            editor.setMapper(mapper);
            editor.init();
            configureComponent();
            String title = getTitle();
            String tooltip = getTooltip();
            JPanel panel = editor.getPanel();
            container.add(title, tooltip, panel, group);
            registry.add(this, panel);
        }
        select();
    }

    private Editor newInstance() {
        Class<? extends Editor> clazz = identifier.getMetadata()
                .getEditorClass();
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void configureComponent();

    @Override
    public boolean isOpened() {
        return editor != null;
    }

    @Override
    public void select() throws NotOpenedException {
        checkIfOpened();
        if (isSelected())
            return;
        container.setSelected(editor.getPanel(), group);
    }

    @Override
    public boolean isSelected() throws NotOpenedException {
        checkIfOpened();
        return container.isSelected(editor.getPanel(), group);
    }

    @Override
    public void undo() throws NotOpenedException {
        checkIfOpened();
        editor.undo();
    }

    @Override
    public void redo() throws NotOpenedException {
        checkIfOpened();
        editor.redo();
    }

    @Override
    public EditorIdentifier getIdentifier() {
        return identifier;
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
