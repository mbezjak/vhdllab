package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.applets.editor.automat.entityTable.NumberBox;

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

public class CircuitInterfaceWizardPage extends AbstractWizardPage {

    private static final String PAGE_ID = "circuitInterface";

    JTable table;
    BeanTableModel model;
    ActionCommand addAction;
    ActionCommand removeAction;

    public CircuitInterfaceWizardPage() {
        super(PAGE_ID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl() {
        List<CircuitInterfaceObject> list = new ArrayList<CircuitInterfaceObject>();
        list.add(new CircuitInterfaceObject());
        model = new BeanTableModel(CircuitInterfaceObject.class, list,
                getMessageSource()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected String[] createColumnPropertyNames() {
                return new String[] { "name", "portDirection", "typeName",
                        "from", "to" };
            }

            @Override
            protected Class[] createColumnClasses() {
                return new Class[] { String.class, PortDirection.class,
                        TypeName.class, Integer.class, Integer.class };
            }

        };
        model.setRowNumbers(false);
        table = new JTable(model);
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeAction.setEnabled(!table.getSelectionModel().isSelectionEmpty());
            }
        });

        final JComboBox inComboBox = new JComboBox(new EnumComboBoxModel(
                PortDirection.class));
        JComboBox tipComboBox = new JComboBox(new EnumComboBoxModel(
                TypeName.class));
        JTextField imeSignala = new JTextField("");

        NumberBox brojevi = new NumberBox("0");

        new ComboBoxAutoCompletion(inComboBox);
        new ComboBoxAutoCompletion(tipComboBox);

        table.getColumnModel().getColumn(0).setCellEditor(
                new DefaultCellEditor(imeSignala));
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setCellEditor(
                new ComboBoxCellEditor(inComboBox));
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setCellEditor(
                new ComboBoxCellEditor(tipComboBox));
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setCellEditor(
                new DefaultCellEditor(brojevi));
        table.getColumnModel().getColumn(3).setPreferredWidth(65);
        table.getColumnModel().getColumn(4)
                .setCellEditor(new NumberEditorExt());
        table.getColumnModel().getColumn(4).setPreferredWidth(65);
        table.setPreferredScrollableViewportSize(new Dimension(300, 200));

        table.addKeyListener(new EditComboBoxKeyHandler());

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel control = new JPanel(new BorderLayout());
        control.add(createButtons(), BorderLayout.NORTH);
        control.add(scrollPane, BorderLayout.CENTER);
        return control;
    }
    
    @SuppressWarnings("unchecked")
    public List<CircuitInterfaceObject> getPorts() {
        return model.getRows();
    }

    private Component createButtons() {
        addAction = new ActionCommand(PAGE_ID + ".addRow") {
            @Override
            protected void doExecuteCommand() {
                model.addRow(new CircuitInterfaceObject());
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

    class EditComboBoxKeyHandler extends KeyAdapter {
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
                    String name = (String) model.getValueAt(lastRowIndex, 0);
                    if (!StringUtils.isEmpty(name)) {
                        addAction.execute();
                    }
                }
            }
        }
    }

}
