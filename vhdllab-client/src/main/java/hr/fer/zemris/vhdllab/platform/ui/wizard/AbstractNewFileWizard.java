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
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractNewFileWizard extends
        AbstractFormSupportingWizard {

    @Autowired
    private WorkspaceManager manager;

    @Override
    protected void onWizardFinished() {
        try {
            File file = getFile();
            file.setProject(EntityUtils.lightweightClone(file.getProject()));
            file.setType(getFileType());
            file.setData(createData());
            manager.create(file);
        } catch (IllegalStateException e) {
            // hack for NewTestbenchWizard to cancel wizard
            if (!e.getMessage().equals("Dialog canceled")) {
                throw e;
            }
        }
    }

    protected abstract File getFile();

    protected abstract FileType getFileType();

    protected abstract String createData();

    @Override
    protected File getFile(FileForm fileForm) {
        return (File) fileForm.getFormObject();
    }

    @Override
    protected CircuitInterface getCircuitInterface(FileForm fileForm,
            PortWizardPage portPage) {
        File file = getFile(fileForm);
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(portPage.getPorts());
        return ci;
    }

}
