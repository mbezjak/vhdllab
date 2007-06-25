package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * GUI komponenta koja sluzi za prikaz i promjenu svojstava neke komponente
 * 
 * @author Garfield
 */
public class ComponentPropertiesToolbar extends JPanel implements
		PropertyChangeListener {
	private static final long serialVersionUID = 2407077708960921188L;

	
	private static final int TOOLBAR_RESIZE_MODE = JTable.AUTO_RESIZE_ALL_COLUMNS;

	/**
	 * Default names for only 2 columns
	 */
	private static final String[] TOOLBAR_TABLE_HEADER = { "Name", "Value" };
	/**
	 * Table
	 */
	private JTableX propertiesTable = null;
	/**
	 * Controller
	 */
	private ISchemaController controller = null;

	public ComponentPropertiesToolbar(ISchemaController controller) {
		super(new BorderLayout());

		if (controller == null)
			throw new NullPointerException("Controller cannot be null!");

		this.controller = controller;
		initToolbar();
	}

	/**
	 * Init
	 */
	private void initToolbar() {
		// init panel

		setOpaque(true);
	}

	private void initTable() {
		propertiesTable = new JTableX();

		// look
		propertiesTable.setAutoResizeMode(TOOLBAR_RESIZE_MODE);
		propertiesTable.setRowSelectionAllowed(false);
		propertiesTable.setColumnSelectionAllowed(false);

		// put table on panel
		add(propertiesTable, BorderLayout.CENTER);
	}

	/**
	 * Changes property toolbar in order to shows properties of new (selected)
	 * component.
	 * 
	 * @param component
	 *            (selected) component
	 */
	public void showPropertyFor(ISchemaComponent component) {
		// lista parametara
		IParameterCollection paramCollection = null;
		// model koji drzi do izgleda tabele
		ComponentPropertiesToolbarTableModel tableModel = null;

		if (component == null)
			throw new NullPointerException("Schema component cannot be null!");

		paramCollection = component.getParameters();

		clearTable();
		tableModel = buildTableModel(paramCollection);
		resetTableModel(tableModel);
		resetTableRowEditors(tableModel.getTableLines());
		repaint();
	}

	private void resetTableRowEditors(IParameter[] parameters) {
		RowEditorModel rowModel = new RowEditorModel();
		DefaultCellEditor modelEditor = null;

		int row = 0;

		for (IParameter p : parameters) {
			modelEditor = DefaultTableRowEditorFactory.getRowEditorForType(p
					.getType());
			if (modelEditor != null) {
				rowModel.addEditorForRow(row, modelEditor);
			}
			row++;
		}

		propertiesTable.setRowEditorModel(rowModel);
	}

	/**
	 * Resets content of table
	 * 
	 * @param tableModel
	 */
	private void resetTableModel(ComponentPropertiesToolbarTableModel tableModel) {
		initTable();
		propertiesTable.setModel(tableModel);
	}

	/**
	 * Builds new table model
	 * 
	 * @param paramCollection
	 *            list of parameters
	 * @return new table model
	 */
	private ComponentPropertiesToolbarTableModel buildTableModel(
			IParameterCollection paramCollection) {

		return new ComponentPropertiesToolbarTableModel(TOOLBAR_TABLE_HEADER,
				paramCollection);
	}

	/**
	 * Clears content of table
	 */
	private void clearTable() {
		if (propertiesTable != null)
			remove(propertiesTable);

		propertiesTable = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		ISchemaComponentCollection componentCollection = null;
		ISchemaComponent component = null;
		ISchemaInfo info = null;
		Caseless componentName = null;

		componentName = (Caseless) evt.getNewValue();
		info = controller.getSchemaInfo();
		componentCollection = info.getComponents();
		component = componentCollection.fetchComponent(componentName);

		showPropertyFor(component);
	}
}
