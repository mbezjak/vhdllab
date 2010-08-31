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
package hr.fer.zemris.vhdllab.applets.view.simulation;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationListener;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.support.AbstractView;

/**
 * @author Miro Bezjak
 * @version 1.0
 */
public class SimulationErrorsView extends AbstractView implements
        SimulationListener {

    @Autowired
    private SimulationManager simulationManager;

    /** DefaultListModel */
    private DefaultListModel model;

    @Override
    protected JComponent createControl() {
        model = new DefaultListModel();
        JList listContent = new JList(model);
        listContent.setFixedCellHeight(15);

        simulationManager.addListener(this);
        return new JScrollPane(listContent);
    }

    private void setContent(Result result) {
        if (result.isSuccessful()) {
            // TODO ovo ucitat iz bundle-a
            Format formatter = new SimpleDateFormat("HH:mm:ss");
            String time = formatter.format(new Date());
            model.addElement(time + "  Simulation finished successfully.");
        } else {
            model.clear();
            for (String msg : result.getMessages()) {
                model.addElement(msg);
            }
        }
    }

    @Override
    public void compiled(File compiledFile, List<CompilationMessage> messages) {
    }

    @Override
    public void simulated(File simulatedfile, Result result) {
        setContent(result);
        getActiveWindow().getPage().setActiveComponent(this);
    }

}
