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
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.listener.StandaloneEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.StringUtils;
import org.springframework.richclient.factory.AbstractControlFactory;

public abstract class AbstractEditor extends AbstractControlFactory implements
        Editor {

    private final EventPublisher<EditorListener> publisher;
    private boolean modificationsEnabled;
    private boolean modified;
    private File file;
    protected PlatformContainer container;
    private EditorMetadata metadata;

    protected boolean wrapInScrollPane = true;

    public AbstractEditor() {
        this.publisher = new StandaloneEventPublisher<EditorListener>(
                EditorListener.class);
        modificationsEnabled = false;
        modified = false;
    }

    @Override
    public PlatformContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(PlatformContainer container) {
        this.container = container;
    }

    @Override
    protected JComponent createControl() {
        JPanel control = new JPanel(new BorderLayout());
        JComponent userComponent = doInitWithoutData();
        JComponent center = userComponent;

        if (wrapInScrollPane) {
            center = new JScrollPane(userComponent);
        }

        control.add(center, BorderLayout.CENTER);

        return control;
    }

    @Override
    public void dispose() {
        modificationsEnabled = false;
        publisher.removeListeners();
        doDispose();
    }

    @Override
    public void undo() {
    }

    @Override
    public void redo() {
    }

    @Override
    public void setFile(File file) {
        this.file = (file != null ? new File(file, true) : null);
        modificationsEnabled = false;
        doInitWithData(this.file);
        setModified(false);
        modificationsEnabled = true;
    }

    @Override
    public File getFile() {
        String data = null;
        if (isControlCreated()) {
            data = getData();
        }
        if (file != null && data != null) {
            file.setData(data);
        }
        return file != null ? new File(file, true) : null;
    }

    @Override
    public String getFileName() {
        return (file != null ? file.getName() : null);
    }

    @Override
    public String getProjectName() {
        if (file != null) {
            return file.getProject().getName();
        }
        return null;
    }

    @Override
    public boolean setModified(boolean flag) {
        if (!modificationsEnabled) {
            return false;
        }
        if (flag != modified) {
            modified = flag;
            fireModified(getFile(), flag);
            return true;
        }
        return false;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setEditable(boolean flag) {
    }

    /**
     * Fires a modified event in all editor listeners.
     * 
     * @param f
     *            a file containing modified data
     * @param flag
     *            <code>true</code> if editor has been modified or
     *            <code>false</code> otherwise (i.e. an editor has just been
     *            saved)
     */
    private void fireModified(File f, boolean flag) {
        for (EditorListener l : publisher.getListeners()) {
            l.modified(f, flag);
        }
    }

    @Override
    public void highlightLine(int line) {
    }

    @Override
    public EventPublisher<EditorListener> getEventPublisher() {
        return publisher;
    }

    @Override
    public void setMetadata(EditorMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public EditorMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String getTitle() {
        String beanName = StringUtils.uncapitalize(metadata.getClass()
                .getSimpleName());
        beanName = beanName.replace("Metadata", "");
        Object[] args = new Object[] { getFileName(), getProjectName() };
        return getMessage(beanName + ".title", args);
    }

    @Override
    public String getCaption() {
        String beanName = StringUtils.uncapitalize(metadata.getClass()
                .getSimpleName());
        beanName = beanName.replace("Metadata", "");
        String editableMessage;
        if (metadata.isEditable()) {
            editableMessage = container.getMessage("editor.editable.caption");
        } else {
            editableMessage = container.getMessage("editor.readonly.caption");
        }
        Object[] args = new Object[] { getFileName(), getProjectName(),
                editableMessage };
        return getMessage(beanName + ".caption", args);
    }

    protected abstract JComponent doInitWithoutData();

    protected abstract void doInitWithData(File f);

    protected abstract void doDispose();

    protected abstract String getData();

}
