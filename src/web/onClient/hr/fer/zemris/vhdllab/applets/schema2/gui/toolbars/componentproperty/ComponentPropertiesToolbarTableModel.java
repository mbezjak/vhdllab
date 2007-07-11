package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

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
	 * Default names for only 2 columns
	 */
	private static final String[] TOOLBAR_TABLE_HEADER = { "Name", "Value" };

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
	/**
	 * Component name
	 */
	private Caseless componentName = null;
	/**
	 * Schema info for ICommand
	 */
	private ISchemaInfo schemaInfo;
	/**
	 * Property changer
	 */
	private ComponentPropertiesToolbarChange propertyChanger = null;

	public ComponentPropertiesToolbarTableModel(
			IParameterCollection parameters, Caseless component,
			ISchemaInfo schemaInfo) {
		super();

		this.componentName = component;
		this.schemaInfo = schemaInfo;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object,
	 *      int, int)
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {
		System.out.println("CPToolbarTableModle: setValueAt(" + row + ","
				+ column + ")");
		// FIXME Ovo ubuduce nece smjet ovako!
		if (!getComponentPropertiesToolbarChange().ChangeProperty(lines[row],
				value)) {

		}
	}

	private ComponentPropertiesToolbarChange getComponentPropertiesToolbarChange() {
		if (propertyChanger == null) {
			propertyChanger = new ComponentPropertiesToolbarChange(
					componentName, schemaInfo);
		}

		return propertyChanger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (column < 2) {
			return TOOLBAR_TABLE_HEADER[column];
		} else {
			return super.getColumnName(column);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		if (lines != null) {
			return lines.length;
		} else {
			return 0;
		}
	}

}
