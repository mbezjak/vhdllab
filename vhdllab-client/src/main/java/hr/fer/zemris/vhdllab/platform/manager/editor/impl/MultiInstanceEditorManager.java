package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewMetadata;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public class MultiInstanceEditorManager extends
        AbstractComponentManager<Editor> implements EditorManager {

    private static final String MODIFIED_PREFIX = "*";

    @Autowired
    private ISystemContainer systemContainer;

    @Autowired
    private FileManager manager;
    @Resource(name = "singleSaveDialogManager")
    private DialogManager dialogManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    public MultiInstanceEditorManager(EditorIdentifier identifier) {
        super(identifier, ComponentGroup.EDITOR);
    }
    
    @Override
    protected Class<? extends View> getComponent(ViewMetadata metadata) {
        return ((EditorMetadata) metadata).getEditorClass();
    }

    @Override
    protected void configureComponent() {
        component.setSystemContainer(systemContainer);
        component.setFile(getFile());
        EventPublisher<EditorListener> publisher = component
                .getEventPublisher();
        publisher.addListener(new EditorModifiedListener());
    }

    @Override
    public void doClose() {
        save(true);
        super.doClose();
    }

    @Override
    public void save(boolean withDialog) {
        checkIfOpened();
        if (component.isSaveable() && component.isModified()) {
            boolean shouldContinue = true;
            FileInfo file = getFile();
            if (withDialog) {
                ProjectInfo project = mapper.getProject(file.getProjectId());
                shouldContinue = dialogManager.showDialog(file.getName(),
                        project.getName());
            }
            if (shouldContinue) {
                manager.save(file);
                component.setModified(false);
            }
        }
    }

    private FileInfo getFile() {
        return ((EditorIdentifier) identifier).getInstanceModifier();
    }

    void resetEditorTitle(boolean modified) {
        String title = getTitle();
        if (modified) {
            title = MODIFIED_PREFIX + title;
        }
        container.setTitle(title, component.getPanel(), group);
    }

    class EditorModifiedListener implements EditorListener {
        @Override
        public void modified(FileInfo file, boolean flag) {
            resetEditorTitle(flag);
        }
    }

}
