package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.SwingComponent;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellEditor;



/**
 * Model za "napredni" JTable, omogucuje dodavanje razlicitih
 * <code>TableCellEditor</code> za isti stupac u JTable
 * 
 */
public class RowEditorModel {
	private Map<Integer, TableCellEditor> data;

	public RowEditorModel() {
		data = new HashMap<Integer, TableCellEditor>();
	}

	public void addEditorForRow(int row, TableCellEditor e) {
		data.put(Integer.valueOf(row), e);
	}

	public void removeEditorForRow(int row) {
		data.remove(Integer.valueOf(row));
	}

	public TableCellEditor getEditor(int row) {
		return data.get(Integer.valueOf(row));
	}
}
