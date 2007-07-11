package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		add(new JLabel("No component selected"), BorderLayout.NORTH);
	}

	private void initTable(ComponentPropertiesToolbarTableModel tableModel) {
		propertiesTable = new JTableX();

		// look
		propertiesTable.setAutoResizeMode(TOOLBAR_RESIZE_MODE);
		propertiesTable.setRowSelectionAllowed(false);
		propertiesTable.setColumnSelectionAllowed(false);

		JScrollPane sPane = new JScrollPane(propertiesTable);
		sPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// put table on panel
		add(sPane, BorderLayout.CENTER);
		add(new JLabel(((Caseless) tableModel.getTableLines()[1].getValue())
				.toString()), BorderLayout.NORTH);
		setPreferredSize(new Dimension(250, getHeight()));
		revalidate();
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
		tableModel = buildTableModel(paramCollection, component, controller
				.getSchemaInfo());
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
		initTable(tableModel);
		propertiesTable.setModel(tableModel);
	}

	/**
	 * Builds new table model
	 * 
	 * @param paramCollection
	 *            list of parameters
	 * @param schemaInfo
	 * @param component
	 * @return new table model
	 */
	private ComponentPropertiesToolbarTableModel buildTableModel(
			IParameterCollection paramCollection, ISchemaComponent component,
			ISchemaInfo schemaInfo) {

		Caseless componentName = component.getName();

		return new ComponentPropertiesToolbarTableModel(paramCollection,
				componentName, schemaInfo);
	}

	/**
	 * Clears content of table
	 */
	private void clearTable() {

		removeAll();
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
