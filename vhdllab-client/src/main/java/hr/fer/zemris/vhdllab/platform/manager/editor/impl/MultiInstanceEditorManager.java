package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;

import javax.annotation.Resource;
import javax.swing.JPanel;

public class MultiInstanceEditorManager extends AbstractEditorManager {

    private static final String MODIFIED_PREFIX = "*";

    @Resource(name = "singleSaveDialogManager")
    private DialogManager dialogManager;

    public MultiInstanceEditorManager(EditorIdentifier identifier) {
        super(identifier, ComponentGroup.EDITOR);
    }

    @Override
    protected void configureComponent() {
        editor.setFile(identifier.getInstanceModifier());
        EventPublisher<EditorListener> publisher = editor.getEventPublisher();
        publisher.addListener(new EditorModifiedListener());
    }

    @Override
    public void close() throws NotOpenedException {
        checkIfOpened();
        doClose();
    }

    protected void doClose() {
        save(true);
        editor.dispose();
        JPanel panel = editor.getPanel();
        container.remove(panel, group);
        registry.remove(panel);
        // ConfigurableBeanFactory beanFactory = context.getBeanFactory();
        // beanFactory.destroyBean(identifier.getViewName(), component);
        editor = null;
    }

    @Override
    public boolean save(boolean withDialog) {
        checkIfOpened();
        boolean editorSaved = true;
        if (editor.isSavable() && editor.isModified()) {
            boolean shouldContinue = true;
            FileInfo file = editor.getFile();
            if (withDialog) {
                ProjectInfo project = mapper.getProject(file.getProjectId());
                shouldContinue = dialogManager.showDialog(file.getName(),
                        project.getName());
            }
            if (shouldContinue) {
                fileManager.save(file);
                editor.setModified(false);
            } else {
                editorSaved = false;
            }
        }
        return editorSaved;
    }

    void resetEditorTitle(boolean modified) {
        String title = getTitle();
        if (modified) {
            title = MODIFIED_PREFIX + title;
        }
        container.setTitle(title, editor.getPanel(), group);
    }

    class EditorModifiedListener implements EditorListener {
        @Override
        public void modified(FileInfo file, boolean flag) {
            resetEditorTitle(flag);
        }
    }

}
