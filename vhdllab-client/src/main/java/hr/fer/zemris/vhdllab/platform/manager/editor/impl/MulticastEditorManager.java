package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class MulticastEditorManager implements EditorManager {

    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Resource(name = "saveDialogManager")
    private DialogManager saveDialog;

    protected final List<EditorManager> managers;

    public MulticastEditorManager(List<EditorManager> managers) {
        Validate.notNull(managers, "Editor managers can't be null");
        this.managers = new ArrayList<EditorManager>(managers);
        for (EditorManager man : this.managers) {
            if (!man.isOpened()) {
                throw new IllegalArgumentException(
                        "All components must be opened");
            }
        }
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public boolean isOpened() {
        return !managers.isEmpty();
    }

    @Override
    public void select() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public void close() {
        close(true);
    }

    @Override
    public void close(boolean saveFirst) throws NotOpenedException {
        if(saveFirst) {
            save(true);
        }
        for (EditorManager man : managers) {
            man.close(saveFirst);
        }
    }

    @Override
    public boolean save(boolean withDialog) {
        if (managers.size() == 1) {
            return managers.get(0).save(withDialog);
        }

        List<FileIdentifier> identifiers = new ArrayList<FileIdentifier>();
        Map<FileIdentifier, EditorManager> map = new HashMap<FileIdentifier, EditorManager>();
        for (EditorManager em : managers) {
            EditorIdentifier editorIdentifier = em.getIdentifier();
            if (editorIdentifier.getMetadata().isSaveable()) {
                FileInfo file = editorIdentifier.getInstanceModifier();
                ProjectInfo project = mapper.getProject(file.getProjectId());
                FileIdentifier fileIdentifier = new FileIdentifier(project
                        .getName(), file.getName());
                identifiers.add(fileIdentifier);
                map.put(fileIdentifier, em);
            }
        }
        List<FileIdentifier> resourcesToSave = saveDialog
                .showDialog(identifiers);
        if (resourcesToSave == null || resourcesToSave.isEmpty()) {
            return false;
        }
        for (FileIdentifier i : resourcesToSave) {
            EditorManager em = map.get(i);
            em.save(false);
        }
        return true;
    }

    @Override
    public void undo() throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public void redo() throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public EditorIdentifier getIdentifier() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

}
