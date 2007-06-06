package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;

import javax.swing.table.DefaultTableModel;

public class SComponentPropertiesToolbarTableModel extends DefaultTableModel {
	private static final long serialVersionUID = -5235065416983970235L;
	private String[] properties = { "Proba1", "Proba2", "Proba3", "Proba4" };

	public SComponentPropertiesToolbarTableModel(String[] columnNames,
			IParameterCollection parameters) {
		// super(columnNames, parameters.count());
		super(columnNames, 4);// FIXME ovo je uhardkodirano
		// TODO nakon alexa, ovdje se treba nastaviti posao

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return properties[row];
		}

		return super.getValueAt(row, column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		// FIXME ovo cu naknadno morat popravit....
		if (column == 0) {
			return false;
		}

		return super.isCellEditable(row, column);
	}
}
