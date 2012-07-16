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

import javax.swing.JComponent;
import javax.swing.JList;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.ListBinding;

public class FileListBinding extends ListBinding {

    private FileSelectionComponent fileComponent;

    @SuppressWarnings("unchecked")
    public FileListBinding(FileSelectionComponent fileComponent,
            FormModel formModel, String formFieldPath, Class requiredSourceClass) {
        super(fileComponent.getList(), formModel, formFieldPath,
                requiredSourceClass);
        this.fileComponent = fileComponent;
    }

    @Override
    public JComponent getComponent() {
        return fileComponent;
    }

    @Override
    public JList getList() {
        return fileComponent.getList();
    }

}
