package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;

import javax.swing.table.DefaultTableModel;

/**
 * Table model
 * 
 * @author Garfield
 * 
 */
public class ComponentPropertiesToolbarTableModel extends DefaultTableModel {
	private static final long serialVersionUID = -5235065416983970235L;

	/**
	 * Default message for empty propery list
	 */
	private static final String TOOLBAR_TABLE_NO_PROPERTIES = "Empty property list";

	/**
	 * Parameters in one component
	 */
	private IParameterCollection parameters = null;

	/**
	 * Values in table
	 */
	private IParameter[] lines = null;

	public ComponentPropertiesToolbarTableModel(String[] columnNames,
			IParameterCollection parameters) {
		super(((parameters.count() > 0) ? columnNames : new String[] { "" }),
				parameters.count() + 1);

		this.parameters = parameters;
		lines = new IParameter[parameters.count()];
		buildLines();
	}

	private void buildLines() {
		int line = 0;
		for (IParameter param : parameters) {
			lines[line++] = param;
		}
	}

	/**
	 * Lines in table
	 * 
	 * @return
	 */
	public IParameter[] getTableLines() {
		return lines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (parameters.count() != 0) {
			if (column == 0) {
				return lines[row].getName();
			} else if (column == 1) {
				return lines[row].getValue();
			}
		} else {
			if (column == 0 && row == 0) {
				return TOOLBAR_TABLE_NO_PROPERTIES;
			}
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
		if (column == 0) {
			return false;
		}

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
