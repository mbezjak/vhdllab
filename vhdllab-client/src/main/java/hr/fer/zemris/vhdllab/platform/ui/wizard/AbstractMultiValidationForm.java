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
package hr.fer.zemris.vhdllab.platform.ui.wizard;


import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;

import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

public abstract class AbstractMultiValidationForm extends AbstractForm
        implements AboutToShowHook {

    protected JComponent componentToGiveFocusTo;

    public AbstractMultiValidationForm(Object formObject, String formId) {
        super(FormModelUtils.createFormModel(formObject), formId);
    }

    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        doBuildForm(builder);

        JComponent control = builder.getForm();
        control.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (componentToGiveFocusTo != null) {
                    componentToGiveFocusTo.requestFocusInWindow();
                }
            }
        });
        return control;
    }

    protected void focusOnBeginning(JComponent c) {
        componentToGiveFocusTo = c;
    }

    @Override
    public void onAboutToShow() {
    }

    protected abstract void doBuildForm(TableFormBuilder builder);

}
