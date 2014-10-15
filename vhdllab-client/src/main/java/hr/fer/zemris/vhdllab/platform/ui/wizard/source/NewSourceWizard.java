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
package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSourceWizard extends AbstractNewFileWizard {

    private FileForm fileForm;
    private PortWizardPage portWizardPage;

    @Override
    public void addPages() {
        fileForm = new FileForm();
        addForm(fileForm);

        portWizardPage = new PortWizardPage();
        addPage(portWizardPage);
    }

    @Override
    protected File getFile() {
        return getFile(fileForm);
    }

    @Override
    protected FileType getFileType() {
        return FileType.SOURCE;
    }

    @Override
    protected String createData() {
        CircuitInterface ci = getCircuitInterface(fileForm, portWizardPage);
        StringBuilder sb = new StringBuilder(100 + ci.getPorts().size() * 20);
        sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
        sb.append("-- warning: this file will not be saved if:\n");
        sb.append("--     * following entity block contains any syntactic errors (e.g. port list isn't separated with ; character)\n");
        sb.append("--     * following entity name and current file name differ (e.g. if file is named mux41 then entity must also be named mux41 and vice versa)\n");
        sb.append(ci.toString()).append("\n\n");
        sb.append("ARCHITECTURE arch OF ").append(ci.getName());
        sb.append(" IS \n\nBEGIN\n\nEND arch;");
        return sb.toString();
    }

}
