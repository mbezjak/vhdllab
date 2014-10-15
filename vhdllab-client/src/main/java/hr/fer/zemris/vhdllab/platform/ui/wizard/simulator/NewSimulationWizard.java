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
package hr.fer.zemris.vhdllab.platform.ui.wizard.simulator;

import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractFormSupportingWizard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.richclient.form.Form;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSimulationWizard extends AbstractFormSupportingWizard {

    @Autowired
    private WorkspaceManager manager;

    protected Form simulationFileForm;

    @Override
    public void addPages() {
        simulationFileForm = new SimulationFileForm();
        addForm(simulationFileForm);
    }

    @Override
    protected void onWizardFinished() {
        SimulationFile simulationFile = (SimulationFile) simulationFileForm.getFormObject();
        manager.saveSimulation(simulationFile.getTargetFile(), simulationFile.getSimulationName());
    }

}
