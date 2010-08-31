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
package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.CompilableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;

import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.stereotype.Component;

@Component
public class FileListBinder extends AbstractBinder {

    @Autowired
    private WorkspaceManager workspaceManager;

    protected FileListBinder() {
        super(File.class, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl(Map context) {
        Predicate fileFilter = CompilableFilesFilter.getInstance();
        Predicate projectFilter = new NotEmptyProjectFilter(workspaceManager,
                fileFilter);
        return new FileSelectionComponent(workspaceManager, projectFilter,
                fileFilter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel,
            String formPropertyPath, Map context) {
        FileSelectionComponent fileComponent = (FileSelectionComponent) control;
        return new FileListBinding(fileComponent, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

}
