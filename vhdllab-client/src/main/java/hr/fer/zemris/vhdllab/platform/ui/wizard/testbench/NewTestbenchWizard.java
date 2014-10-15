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
package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.CombinatorialTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.SingleClockTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.EditableSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.InitTimingDialog;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.form.Form;

public class NewTestbenchWizard extends AbstractNewFileWizard {

    @Autowired
    private MetadataExtractionService metadataExtractionService;

    protected Form testbenchFileForm;

    @Override
    public void addPages() {
        testbenchFileForm = new TestbenchFileForm();
        addForm(testbenchFileForm);
    }

    @Override
    protected File getFile() {
        TestbenchFile testbenchFile = (TestbenchFile) testbenchFileForm
                .getFormObject();
        File file = new File();
        file.setName(testbenchFile.getTestbenchName());
        file.setProject(testbenchFile.getTargetFile().getProject());
        return file;
    }

    @Override
    protected FileType getFileType() {
        return FileType.TESTBENCH;
    }

    @Override
    protected String createData() {
        TestbenchFile testbenchFile = (TestbenchFile) testbenchFileForm
                .getFormObject();
        String string = oldCode(testbenchFile.getTestbenchName(), testbenchFile
                .getTargetFile());
        if (string == null) {
            performCancel();
            throw new IllegalStateException("Dialog canceled");
        }
        return string;
    }

    private String oldCode(String testbench, File targetFile) {
        java.awt.Component parent = null;

        CircuitInterface ci = metadataExtractionService
                .extractCircuitInterface(targetFile.getId());

        while (true) {
            InitTimingDialog initTimingDialog = new InitTimingDialog(parent,
                    true, ci, testbench, targetFile.getProject().getName());
            initTimingDialog.startDialog();
            if (initTimingDialog.getOption() != InitTimingDialog.OK_OPTION)
                return null;

            Testbench tb = null;

            try {
                tb = this.getInitialTestbench(initTimingDialog, targetFile
                        .getName());
                this.addSignals(tb, ci);
                return tb.toXml();
            } catch (UniformTestbenchException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error creating testbench", JOptionPane.ERROR_MESSAGE);
                continue;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addSignals(Testbench tb, CircuitInterface ci) {
        Signal s = null;
        for (Port p : ci.getPorts()) {
            try {
                if (p.isIN()) {
                    if (p.isScalar()) {
                        s = new ScalarSignal(p.getName());
                    } else {
                        short d = (short) (1 + Math
                                .abs(p.getFrom() - p.getTo()));
                        s = new VectorSignal(p.getName(), d, VectorDirection
                                .valueOf(p.getDirectionName().toLowerCase()));
                    }
                }

                if (s != null) {
                    tb.addSignal((EditableSignal) s);
                }
            } catch (UniformSignalException e) {
                e.printStackTrace();
            } catch (UniformTestbenchException e) {
                e.printStackTrace();
            }
        }
    }

    private Testbench getInitialTestbench(InitTimingDialog initTimingDialog,
            String sourceName) throws UniformTestbenchException {

        Testbench tb = null;
        TimeScale timeScale = TimeScale
                .valueOf(initTimingDialog.getTimeScale());

        if (Math.log(TimeScale.getMultiplier(timeScale))
                + Math.log(initTimingDialog.getInitialLengthOfTestbench()) >= Math
                .log(Long.MAX_VALUE)) {
            throw new UniformTestbenchException(
                    "Testbench length is too large. Change the time scale.");
        }

        long testBenchLength = TimeScale.getMultiplier(timeScale)
                * initTimingDialog.getInitialLengthOfTestbench();

        if (initTimingDialog.isCombinatorial()) {
            CombinatorialTestbench.Properties p = new CombinatorialTestbench.Properties();
            p.assignInputsTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getAssignInputs();
            p.checkOutputsTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getCheckOutputs();
            tb = new CombinatorialTestbench(sourceName, testBenchLength,
                    timeScale, p);
        } else {
            SingleClockTestbench.Properties p = new SingleClockTestbench.Properties();
            if (initTimingDialog.isRisingEdgeSelected()) {
                p.changeStateEdge = ChangeStateEdge.rising;
            } else {
                p.changeStateEdge = ChangeStateEdge.falling;
            }
            p.clockSignalName = initTimingDialog.getClockSignal();
            p.clockTimeHigh = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getClockTimeHigh();
            p.clockTimeLow = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getClockTimeLow();
            p.inputSetupTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getInputSetupTime();

            tb = new SingleClockTestbench(sourceName, testBenchLength,
                    timeScale, p);
        }

        return tb;
    }

}
