package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DefaultEditorManagerFactory implements EditorManagerFactory {

    @Autowired
    private ConfigurableApplicationContext context;
    @Resource(name = "groupBasedComponentContainer")
    private ComponentContainer container;
    @Autowired
    private EditorRegistry registry;
    @Autowired
    private WizardRegistry wizardRegistry;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    private final ComponentGroup group = ComponentGroup.EDITOR;

    @Override
    public EditorManager get(FileInfo file) {
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
        return get(container.getSelected(group));
    }

    @Override
    public EditorManager getAll() {
        return createManager(container.getAll(group));
    }

    @Override
    public EditorManager getAllAssociatedWithProject(Caseless projectName) {
        Validate.notNull(projectName, "Project name can't be null");
        List<JPanel> editors = container.getAll(group);
        List<JPanel> editorsWithSpecifiedProjectName = new ArrayList<JPanel>();
        for (JPanel panel : editors) {
            FileInfo file = get(panel).getIdentifier().getInstanceModifier();
            ProjectInfo project = mapper.getProject(file.getProjectId());
            if (project.getName().equals(projectName)) {
                editorsWithSpecifiedProjectName.add(panel);
            }
        }
        return createManager(editorsWithSpecifiedProjectName);
    }

    @Override
    public EditorManager getAllButSelected() {
        return createManager(container.getAllButSelected(group));
    }

    private EditorManager configureManager(EditorManager manager) {
        String beanName = StringUtils.uncapitalize(manager.getClass()
                .getSimpleName());
        context.getBeanFactory().configureBean(manager, beanName);
        return manager;
    }

    private EditorManager get(JPanel panel) {
        if (panel == null) {
            return new NoSelectionEditorManager();
        }
        return registry.get(panel);
    }

    private EditorManager createManager(List<JPanel> components) {
        List<EditorManager> managers = new ArrayList<EditorManager>(components
                .size());
        for (JPanel panel : components) {
            managers.add(get(panel));
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
