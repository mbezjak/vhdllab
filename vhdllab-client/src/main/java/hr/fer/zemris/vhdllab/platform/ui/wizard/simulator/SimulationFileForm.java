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

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.SimulatableFilesFilter;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileSelectionComponent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class SimulationFileForm extends AbstractMultiValidationForm {

    private FileSelectionComponent fileComponent;

    public SimulationFileForm() {
        super(new SimulationFile(), "newSimulationFile");
    }

    public FileSelectionComponent getFileComponent() {
        return fileComponent;
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        fileComponent = (FileSelectionComponent) builder.add("targetFile")[1];
        fileComponent.setFileFilter(SimulatableFilesFilter.getInstance());
        focusOnBeginning(fileComponent.getList());
        builder.row();
        builder.add("simulationName");

        installSuggestionSimulationName();
    }

    @Override
    public void onAboutToShow() {
        fileComponent.selectLastProject();
    }

    private void installSuggestionSimulationName() {
        getFormModel().getValueModel("targetFile").addValueChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        File selectedFile = (File) evt.getNewValue();
                        String testbenchName = null;
                        if (selectedFile != null) {
                            testbenchName = selectedFile.getName() + "_sim";
                        }
                        getFormModel().getValueModel("simulationName").setValue(
                                testbenchName);
                    }
                });
    }

}
