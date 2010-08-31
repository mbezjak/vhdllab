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
package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.table.NumberEditorExt;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.list.ComboBoxAutoCompletion;
import org.springframework.richclient.table.BeanTableModel;
import org.springframework.richclient.wizard.AbstractWizardPage;

public class PortWizardPage extends AbstractWizardPage {

    private static final int DEFAULT_MINIMUM_PORT_COUNT = 0;
    private static final int DEFAULT_MAXIMUM_PORT_COUNT = 30;
    private static final String PAGE_ID = "newPorts";

    protected JTable table;
    protected BeanTableModel model;
    protected ActionCommand addAction;
    protected ActionCommand removeAction;

    private int minimumPortCount = DEFAULT_MINIMUM_PORT_COUNT;
    private int maximumPortCount = DEFAULT_MAXIMUM_PORT_COUNT;
    private PortValidationReporter reporter;

    public PortWizardPage() {
        super(PAGE_ID);
    }

    public int getMinimumPortCount() {
        return minimumPortCount;
    }

    public void setMinimumPortCount(int minimumPortCount) {
        this.minimumPortCount = minimumPortCount;
    }

    public int getMaximumPortCount() {
        return maximumPortCount;
    }

    public void setMaximumPortCount(int maximumPortCount) {
        this.maximumPortCount = maximumPortCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl() {
        model = new BeanTableModel(Port.class, new ArrayList<Port>(),
                getMessageSource()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String[] createColumnPropertyNames() {
                return new String[] { "name", "direction", "from", "to" };
            }

            @Override
            protected Class[] createColumnClasses() {
                return new Class[] { String.class, PortDirection.class,
                        Integer.class, Integer.class };
            }

        };
        reporter = new PortValidationReporter(model, this, minimumPortCount,
                maximumPortCount);
        table = new JTable(model);
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        removeAction.setEnabled(!table.getSelectionModel()
                                .isSelectionEmpty());
                    }
                });

        final JComboBox inComboBox = new JComboBox(new EnumComboBoxModel(
                PortDirection.class));
        JTextField portName = new JTextField();

        new ComboBoxAutoCompletion(inComboBox);

        table.getColumnModel().getColumn(0).setPreferredWidth(7);
        table.getColumnModel().getColumn(1).setCellEditor(
                new DefaultCellEditor(portName));
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setCellEditor(
                new ComboBoxCellEditor(inComboBox));
        table.getColumnModel().getColumn(3)
                .setCellEditor(new NumberEditorExt());
        table.getColumnModel().getColumn(4)
                .setCellEditor(new NumberEditorExt());
        table.setPreferredScrollableViewportSize(new Dimension(300, 200));

        table.addKeyListener(new EditComboBoxKeyHandler());

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel control = new JPanel(new BorderLayout());
        control.add(createButtons(), BorderLayout.NORTH);
        control.add(scrollPane, BorderLayout.CENTER);
        return control;
    }

    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        validate();
    }
    
    private void validate() {
        reporter.validate();
    }

    @SuppressWarnings("unchecked")
    public List<Port> getPorts() {
        return model.getRows();
    }

    private Component createButtons() {
        addAction = new ActionCommand(PAGE_ID + ".addRow") {
            @Override
            protected void doExecuteCommand() {
                Port port = new Port();
                port.setDirection(PortDirection.IN);
                model.addRow(port);
            }
        };
        removeAction = new ActionCommand(PAGE_ID + ".removeRow") {
            @Override
            protected void onButtonAttached(AbstractButton button) {
                this.setEnabled(false); // remove action is initially disabled
                super.onButtonAttached(button);
            }

            @Override
            protected void doExecuteCommand() {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    TableCellEditor cellEditor = table.getCellEditor();
                    if (cellEditor != null) {
                        cellEditor.stopCellEditing();
                    }
                    model.remove(selectedRow);
                    int rowToSelect = Math.min(selectedRow,
                            model.getRowCount() - 1);
                    if (rowToSelect != -1) {
                        table.getSelectionModel().setSelectionInterval(
                                rowToSelect, rowToSelect);
                    }
                }
            }
        };
        getCommandConfigurer().configure(addAction);
        getCommandConfigurer().configure(removeAction);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addAction.createButton());
        buttonPanel.add(removeAction.createButton());
        JPanel control = new JPanel(new BorderLayout());
        control.add(buttonPanel, BorderLayout.WEST);
        return control;
    }

    protected class EditComboBoxKeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();
                if (col != -1 && new IntRange(1, 2).containsInteger(col)) {
                    table.editCellAt(row, col);
                    Component editor = table.getEditorComponent();
                    if (editor != null) {
                        editor.requestFocusInWindow();
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                int lastRowIndex = model.getRowCount() - 1;
                if (table.getSelectionModel().isSelectedIndex(lastRowIndex)) {
                    String name = (String) model.getValueAt(lastRowIndex, 1);
                    if (!StringUtils.isEmpty(name)) {
                        addAction.execute();
                    }
                }
            }
        }
    }

}
