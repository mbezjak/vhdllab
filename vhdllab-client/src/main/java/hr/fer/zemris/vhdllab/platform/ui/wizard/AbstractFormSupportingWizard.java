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

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.AbstractWizard;
import org.springframework.richclient.wizard.WizardPage;
import org.springframework.util.Assert;

public abstract class AbstractFormSupportingWizard extends AbstractWizard {

    private String id;

    private List<Form> formPages = new ArrayList<Form>();

    public AbstractFormSupportingWizard() {
        this.id = BeanUtil.beanName(getClass());
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public WizardPage addForm(Form formPage) {
        Assert.notNull(formPage, "The form page cannot be null");
        formPages.add(formPage);
        WizardPage wizardPage = new AboutToShowForwardingFormBackedWizardPage(
                formPage);
        addPage(wizardPage);
        return wizardPage;
    }

    @Override
    protected boolean onFinish() {
        for (Form form : formPages) {
            form.commit();
        }
        onWizardFinished();
        return true;
    }

    protected abstract void onWizardFinished();

    // ************************************************************************
    // Supporting methods:
    // ************************************************************************

    protected File getFile(FileForm fileForm) {
        return (File) fileForm.getFormObject();
    }

    protected CircuitInterface getCircuitInterface(FileForm fileForm,
            PortWizardPage portPage) {
        File file = getFile(fileForm);
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(portPage.getPorts());
        return ci;
    }

    protected File createFile(Project project, String name, FileType type,
            String data) {
        File file = new File(name, type, data);
        file.setProject(new Project(project));
        return file;
    }

}
