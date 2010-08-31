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
package hr.fer.zemris.vhdllab.applets.view.compilation;

import hr.fer.zemris.vhdllab.applets.texteditor.ViewVhdlEditorMetadata;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationListener;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.ui.MouseClickAdapter;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.awt.event.MouseEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CompilationErrorsView extends AbstractView implements
        SimulationListener {

    @Autowired
    private SimulationManager simulationManager;
    @Autowired
    private EditorManagerFactory editorManagerFactory;

    /** DefaultListModel */
    private DefaultListModel model;

    private File file = null;

    @Override
    protected JComponent createControl() {
        model = new DefaultListModel();
        final JList listContent = new JList(model);
        listContent.setFixedCellHeight(15);
        listContent.addMouseListener(new MouseClickAdapter() {
            @Override
            protected void onDoubleClick(MouseEvent e) {
                String selectedValue = (String) listContent.getSelectedValue();
                highlightError(selectedValue);
            }
        });

        simulationManager.addListener(this);
        return new JScrollPane(listContent);
    }

    private void setContent(List<CompilationMessage> messages) {
        if (messages.isEmpty()) {
            Format formatter = new SimpleDateFormat("HH:mm:ss");
            String time = formatter.format(new Date());
            model.addElement(time + "  Compilation finished successfully.");
            return;
        }

        model.clear();
        for (CompilationMessage msg : messages) {
            StringBuilder sb = new StringBuilder(msg.getText().length() + 20);
            sb.append(msg.getEntityName()).append(":").append(msg.getRow())
                    .append(":").append(msg.getColumn()).append(":").append(
                            msg.getText());
            model.addElement(sb.toString());
        }
    }

    /**
     * Uzima string te se taj string usporeduje s uzorkom. Ako uzorak postoji
     * unutar tog stringa, iz njega se vade ime datoteke i linija u kojoj se
     * dogodila greska i pozivaju se odgovarajuce metode za pozicioniranje na
     * konkretnu datoteku i liniju u kojoj se dogodila greska. Ako uzorak ne
     * postoji ne dogada se nista.
     * 
     * @param error
     *            Linija koju je generira VHDL simulator
     */
    void highlightError(String error) {
        if (file == null) {
            return;
        }
        Pattern pattern = Pattern.compile("([^:]+):([^:]+):([^:]+):(.+)");
        Matcher matcher = pattern.matcher(error);
        if (matcher.matches()) {
            EditorManager em;
            if (file.getType().equals(FileType.SOURCE)) {
                em = editorManagerFactory.get(file);
            } else {
                EditorIdentifier identifier = new EditorIdentifier(
                        new ViewVhdlEditorMetadata(), file);
                em = editorManagerFactory.get(identifier);
            }
            em.open();
            int line = Integer.valueOf(matcher.group(2));
            em.highlightLine(line);
        }
    }

    @Override
    public void compiled(File compiledFile, List<CompilationMessage> messages) {
        setContent(messages);
        getActiveWindow().getPage().setActiveComponent(this);
        this.file = compiledFile;
    }

    @Override
    public void simulated(File simulatedfile, Result result) {
    }

}
