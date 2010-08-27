package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Napredna komponenta JTable-a koja omogucuje da razliciti retci u jednom
 * stupcu imaju razlicite "editore"
 * 
 */
public class JTableX extends JTable {
	private static final long serialVersionUID = 7277180546418776245L;
	protected RowEditorModel rm;

	public JTableX() {
		super();
		rm = null;
	}

	public JTableX(TableModel tm) {
		super(tm);
		rm = null;
	}

	public JTableX(TableModel tm, TableColumnModel cm) {
		super(tm, cm);
		rm = null;
	}

	public JTableX(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
		super(tm, cm, sm);
		rm = null;
	}

	public JTableX(int rows, int cols) {
		super(rows, cols);
		rm = null;
	}

	@SuppressWarnings("unchecked")
	public JTableX(final Vector rowData, final Vector columnNames) {
		super(rowData, columnNames);
		rm = null;
	}

	public JTableX(final Object[][] rowData, final Object[] colNames) {
		super(rowData, colNames);
		rm = null;
	}

	// new constructor
	public JTableX(TableModel tm, RowEditorModel rm) {
		super(tm, null, null);
		this.rm = rm;
	}

	public void setRowEditorModel(RowEditorModel rm) {
		this.rm = rm;
	}

	public RowEditorModel getRowEditorModel() {
		return rm;
	}

	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		TableCellEditor tmpEditor = null;
		if (rm != null)
			tmpEditor = rm.getEditor(row);
		if (tmpEditor != null)
			return tmpEditor;
		return super.getCellEditor(row, col);
	}
}
