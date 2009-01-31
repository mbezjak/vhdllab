package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class MultiInstanceEditorManager extends AbstractEditorManager {

    private static final String MODIFIED_PREFIX = "*";

    @Resource(name = "singleSaveDialogManager")
    private DialogManager dialogManager;
    @Autowired
    private FileManager fileManager;

    public MultiInstanceEditorManager(EditorIdentifier identifier) {
        super(identifier, ComponentGroup.EDITOR);
    }

    @Override
    protected void configureComponent() {
        editor.setEditable(identifier.getMetadata().isEditable());
        editor.setFile(identifier.getInstanceModifier());
        EventPublisher<EditorListener> publisher = editor.getEventPublisher();
        publisher.addListener(new EditorModifiedListener());
    }

    @Override
    public void close() throws NotOpenedException {
        close(true);
    }

    @Override
    public void close(boolean saveFirst) throws NotOpenedException {
        checkIfOpened();
        if (saveFirst) {
            save(true);
        }
        editor.dispose();
        JPanel panel = editor.getPanel();
        container.remove(panel, group);
        registry.remove(panel);
        editor = null;
    }

    @Override
    public boolean save(boolean withDialog) {
        return save(withDialog, SaveContext.NORMAL);
    }

    @Override
    public boolean save(boolean withDialog, SaveContext context)
            throws NotOpenedException {
        checkIfOpened();
        Validate.notNull(context, "Save context can't be null");
        if (identifier.getMetadata().isSaveable() && isModified()) {
            FileInfo file = editor.getFile();
            if (withDialog) {
                ProjectInfo project = mapper.getProject(file.getProjectId());
                boolean shouldContinue = dialogManager.showDialog(file
                        .getName(), project.getName());
                if (!shouldContinue) {
                    return false;
                }
            }
            fileManager.save(file);
            editor.setModified(false);
        }
        return true;
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
