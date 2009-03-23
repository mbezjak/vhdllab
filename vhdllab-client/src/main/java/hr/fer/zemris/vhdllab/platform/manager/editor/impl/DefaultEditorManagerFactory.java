package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultEditorManagerFactory implements EditorManagerFactory {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private EditorContainer container;
    @Autowired
    private EditorRegistry registry;
    @Autowired
    private WizardRegistry wizardRegistry;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    @Override
    public EditorManager get(File file) {
        Validate.notNull(file, "File can't be null");
        EditorMetadata metadata = wizardRegistry.get(file.getType());
        EditorIdentifier identifier = new EditorIdentifier(metadata, file);
        return get(identifier);
    }

    @Override
    public EditorManager get(EditorIdentifier identifier) {
        Validate.notNull(identifier, "View identifier can't be null");
        EditorManager manager = registry.get(identifier);
        if (manager != null) {
            return manager;
        }
        return configureManager(new MultiInstanceEditorManager(identifier));
    }

    @Override
    public EditorManager getSelected() {
        return get(container.getSelected());
    }

    @Override
    public EditorManager getAll() {
        return createManager(container.getAll());
    }

    @Override
    public EditorManager getAllAssociatedWithProject(Project project) {
        Validate.notNull(project, "Project can't be null");
        List<Editor> editors = container.getAll();
        List<Editor> editorsWithSpecifiedProjectName = new ArrayList<Editor>();
        for (Editor editor : editors) {
            File file = get(editor).getIdentifier().getInstanceModifier();
            if (file.getProject().equals(project)) {
                editorsWithSpecifiedProjectName.add(editor);
            }
        }
        return createManager(editorsWithSpecifiedProjectName);
    }

    @Override
    public EditorManager getAllButSelected() {
        return createManager(container.getAllButSelected());
    }

    private EditorManager configureManager(EditorManager manager) {
        String beanName = StringUtils.uncapitalize(manager.getClass()
                .getSimpleName());
        context.getBeanFactory().configureBean(manager, beanName);
        return manager;
    }

    private EditorManager get(Editor editor) {
        if (editor == null) {
            return new NoSelectionEditorManager();
        }
        return registry.get(editor);
    }

    private EditorManager createManager(List<Editor> components) {
        List<EditorManager> managers = new ArrayList<EditorManager>(components
                .size());
        for (Editor editor : components) {
            managers.add(get(editor));
        }
        EditorManager editorManager;
        if (managers.isEmpty()) {
            editorManager = new NoSelectionEditorManager();
        } else if (managers.size() == 1) {
            editorManager = managers.get(0);
        } else {
            editorManager = configureManager(new MulticastEditorManager(
                    managers));
        }
        return editorManager;
    }

}
