package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.view.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEditorManager extends LocalizationSupport
        implements EditorManager {

    private static final String TITLE_PREFIX = "title.for.";
    private static final String TOOLTIP_PREFIX = "tooltip.for.";
    private static final String EDITABLE_EDITOR_MESSAGE = "tooltip.editor.editable";
    private static final String READONLY_EDITOR_MESSAGE = "tooltip.editor.readonly";

    @Autowired
    protected EditorRegistry registry;
    @Autowired
    protected IdentifierToInfoObjectMapper mapper;
    @Resource(name = "groupBasedComponentContainer")
    protected ComponentContainer container;
    protected final EditorIdentifier identifier;
    protected final ComponentGroup group;
    protected Editor editor;

    @Autowired
    private PlatformContainer platformContainer;

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
            editor.setContainer(platformContainer);
            editor.init();
            configureComponent();
            String title = getTitle();
            String tooltip = getTooltip();
            JPanel panel = editor.getPanel();
            container.add(title, tooltip, panel, group);
            registry.add(this, panel, identifier);
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
    public boolean isModified() throws NotOpenedException {
        checkIfOpened();
        return editor.isModified();
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
        Object[] args = new Object[3];
        EditorMetadata metadata = identifier.getMetadata();
        FileInfo file = identifier.getInstanceModifier();
        if (file != null) {
            ProjectInfo project = mapper.getProject(file.getProjectId());
            args[0] = file.getName();
            args[1] = project.getName();
        }
        if (metadata.isEditable()) {
            args[2] = getMessage(EDITABLE_EDITOR_MESSAGE);
        } else {
            args[2] = getMessage(READONLY_EDITOR_MESSAGE);
        }
        String code = prefix + metadata.getCode();
        return getMessage(code, args);
    }

}
