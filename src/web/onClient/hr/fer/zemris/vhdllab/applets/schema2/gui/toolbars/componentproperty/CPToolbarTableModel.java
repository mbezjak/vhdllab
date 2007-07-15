package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import javax.swing.table.DefaultTableModel;

public class CPToolbarTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 7420025038262028216L;

	/**
	 * Kolekcija parametara prikladnih za TableModel
	 */
	private CPToolbarParameterEnvelopeCollection pec = null;

	public CPToolbarTableModel(CPToolbarParameterEnvelopeCollection pec) {
		this.pec = pec;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return super.getColumnCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return super.getRowCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		return super.getValueAt(row, column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return super.isCellEditable(row, column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {
		// TODO Auto-generated method stub
		super.setValueAt(value, row, column);
	}
	
	
}
