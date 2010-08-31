/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;

public class MulticastEditorManager implements EditorManager {

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
        return true;
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
        boolean shouldSave = true;
        if (saveFirst) {
            shouldSave = save(true);
        }
        if (shouldSave) {
            for (EditorManager man : managers) {
                man.close(false);
            }
        }
    }

    @Override
    public boolean save(boolean withDialog) {
        return save(withDialog, SaveContext.NORMAL);
    }

    @Override
    public boolean save(boolean withDialog, SaveContext context)
            throws NotOpenedException {
        Validate.notNull(context, "Save context can't be null");

        List<File> identifiers = new ArrayList<File>();
        Map<File, EditorManager> map = new HashMap<File, EditorManager>();
        for (EditorManager em : managers) {
            EditorIdentifier editorIdentifier = em.getIdentifier();
            if (editorIdentifier.getMetadata().isSaveable() && em.isModified()) {
                File file = editorIdentifier.getInstanceModifier();
                if (file != null) {
                    identifiers.add(file);
                    map.put(file, em);
                }
            }
        }
        if (!identifiers.isEmpty()) {
            List<File> resourcesToSave = saveDialog.showDialog(identifiers,
                    context);
            if (resourcesToSave == null) {
                return false;
            }
            for (File i : resourcesToSave) {
                EditorManager em = map.get(i);
                em.save(false);
            }
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
    public void highlightLine(int line) throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public boolean isModified() throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public EditorIdentifier getIdentifier() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }
    
}
