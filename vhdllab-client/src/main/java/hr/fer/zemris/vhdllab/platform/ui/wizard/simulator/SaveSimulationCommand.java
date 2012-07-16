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

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.WorkspaceHasSimulatableFileCommandGuard;

import org.springframework.richclient.wizard.Wizard;

public class SaveSimulationCommand extends AbstractShowWizardDialogCommand {

    public SaveSimulationCommand() {
        super();
        setEnabled(false);
    }

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSimulationWizard.class;
    }

    @Override
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        new WorkspaceHasSimulatableFileCommandGuard(wm, wi, this);
    }

}
