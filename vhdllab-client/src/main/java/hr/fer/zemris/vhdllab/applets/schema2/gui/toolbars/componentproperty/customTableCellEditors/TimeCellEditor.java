package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.customTableCellEditors;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.CPToolbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Custom cell editor for Time type. Displays text field and combobox in one
 * line; text field for number part of time, combobox for metric
 * 
 * @author Garfield
 * 
 */
public class TimeCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = -3969944619527318159L;

	/**
	 * Custom editor for TimeCell
	 */
	private JPanel customEditor = null;

	/**
	 * Number part of time
	 */
	private JTextField numberFiled = null;

	/**
	 * Metric part of time
	 */
	private JComboBox metricField = null;

	/**
	 * Construct
	 * 
	 * @param metric
	 */
	public TimeCellEditor(String[] metric) {
		this(new JComboBox(metric));
	}

	public TimeCellEditor(JComboBox metric) {
		metricField = metric;
		initGui();
	}

	/**
	 * Init GUI
	 */
	private void initGui() {
		customEditor = new JPanel(new BorderLayout());
		numberFiled = new JTextField();
		metricField.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
            public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		
		customEditor.add(numberFiled, BorderLayout.CENTER);
		customEditor.add(metricField, BorderLayout.EAST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		Time t = Time.parseTime(value.toString());

		numberFiled.setText(String.valueOf(t.getTimeInterval()));

		metricField.setSelectedItem(t.getTimeMetric().toString());

		if (CPToolbar.DEBUG_MODE) {
			System.out.println("TimeCellEditor: value=" + value.toString());
			System.out.println("TimeCellEditor: getTimeMetric="
					+ t.getTimeMetric().toString());
		}

		return customEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		double broj = 0F;
		String sBroj = numberFiled.getText();

		try {
			broj = Double.parseDouble(numberFiled.getText());
			sBroj = String.valueOf(broj);
		} catch (NumberFormatException e) {
		}

		return sBroj + " " + metricField.getSelectedItem().toString();
	}
}
