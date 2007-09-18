package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.customTableCellEditors;

import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.customdialogs.CustomDialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class ObjectCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = -7490953575849868016L;

	/**
	 * Custom editor for ObjectCell
	 */
	private JPanel customEditor = null;

	/**
	 * Value of OBJECT type
	 */
	private JTextField valueField = null;

	/**
	 * Button for setting value
	 */
	private JButton actionField = null;

	private Object reference = null;

	public ObjectCellEditor(Object reference) {
		super();
		this.reference = reference;
		initGUI();
	}

	private void initGUI() {
		Object panel = null;
		customEditor = new JPanel(new BorderLayout());
		valueField = new JTextField();
		valueField.setEditable(false);

		actionField = new JButton("...");
		actionField.setMargin(new Insets(1, 1, 1, 1));
		actionField.setSize(100, 50);
		if ((panel = CustomDialogs.getDialogForObject(reference)) == null) {
			actionField.setEnabled(false);
		}
		customEditor.add(valueField, BorderLayout.CENTER);
		customEditor.add(actionField, BorderLayout.EAST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return valueField.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		valueField.setText(value.toString());
		return customEditor;

	}

}
