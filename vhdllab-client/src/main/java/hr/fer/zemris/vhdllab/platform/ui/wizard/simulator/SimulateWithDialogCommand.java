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
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.SimulatableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.ui.GuardedActionCommand;
import hr.fer.zemris.vhdllab.platform.ui.MouseClickAdapter;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileSelectionComponent;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.WorkspaceHasSimulatableFileCommandGuard;

import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import org.apache.commons.collections.Predicate;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.list.ListSelectionValueModelAdapter;
import org.springframework.richclient.list.ListSingleSelectionGuard;

public class SimulateWithDialogCommand extends GuardedActionCommand {

    public SimulateWithDialogCommand() {
        setDisplaysInputDialog(true);
        setEnabled(false);
    }

    @Override
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        new WorkspaceHasSimulatableFileCommandGuard(wm, wi, this);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("Executing " + getClass());
        new SimulateDialog().showDialog();
    }

    @SuppressWarnings("synthetic-access")
    protected class SimulateDialog extends TitledApplicationDialog {

        private static final String SIMULATE_TITLE = "simulateDialog.title";
        private static final String SIMULATE_MESSAGE = "simulateDialog.message";
        private static final String SIMULATE_DESCRIPTION = "simulateDialog.description";
        private static final String SIMULATE_COMMAND_ID = "simulateResourceCommand";

        private FileSelectionComponent fileComponent;

        @Override
        protected JComponent createTitledDialogContentPane() {
            Predicate fileFilter = SimulatableFilesFilter.getInstance();
            Predicate projectFilter = new NotEmptyProjectFilter(
                    workspaceManager, fileFilter);
            fileComponent = new FileSelectionComponent(workspaceManager,
                    projectFilter, fileFilter);
            configureComponent(fileComponent);
            return fileComponent;
        }

        private void configureComponent(FileSelectionComponent fc) {
            ListSelectionModel selectionModel = fc.getList()
                    .getSelectionModel();
            ListSelectionValueModelAdapter valueModel = new ListSelectionValueModelAdapter(
                    selectionModel);
            new ListSingleSelectionGuard(valueModel, this);
            fc.selectLastProject();

            fc.getList().addMouseListener(new MouseClickAdapter() {
                @Override
                protected void onDoubleClick(MouseEvent e) {
                    if (isEnabled()) {
                        getFinishCommand().execute();
                    }
                }
            });
        }

        @Override
        protected void onInitialized() {
            setTitle(getMessage(SIMULATE_TITLE));
            setTitlePaneTitle(getMessage(SIMULATE_MESSAGE));
            setDescription(getMessage(SIMULATE_DESCRIPTION));
        }

        @Override
        protected String getFinishCommandId() {
            return SIMULATE_COMMAND_ID;
        }

        @Override
        protected boolean onFinish() {
            simulationManager.simulate(fileComponent.getSelectedFile());
            return true;
        }

    }

}
