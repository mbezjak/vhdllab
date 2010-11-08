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
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class FileForm extends AbstractMultiValidationForm {

    private JComboBox projectCombobox;

    public FileForm() {
        super(new File(), "newFile");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        projectCombobox = (JComboBox) builder.add("project")[1];
        projectCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getFormModel().validate();
            }
        });
        builder.row();
        focusOnBeginning(builder.add("name")[1]);
    }

    @Override
    public void onAboutToShow() {
        projectCombobox.setSelectedIndex(projectCombobox.getItemCount() - 1);
    }

}
