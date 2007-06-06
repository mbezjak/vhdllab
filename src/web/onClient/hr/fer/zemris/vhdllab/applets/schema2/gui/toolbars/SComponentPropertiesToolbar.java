package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars;

import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * GUI komponenta koja sluzi za prikaz i promjenu svojstava neke komponente
 * 
 * @author Garfield
 */
public class SComponentPropertiesToolbar extends JPanel {
	private static final long serialVersionUID = 2407077708960921188L;

	private static final int TOOLBAR_RESIZE_MODE = JTable.AUTO_RESIZE_ALL_COLUMNS;

	private static final String[] TOOLBAR_TABLE_HEADER = { "Name", "Value" };

	private ISchemaComponent schemaComponent = null;
	private JTableX propertiesTable = null;

	public SComponentPropertiesToolbar(ISchemaComponent schemaComponent) {
		super(new BorderLayout());

		if (schemaComponent == null) {
			throw new NullPointerException("Schema component cannot be null!");
		}
		this.schemaComponent = schemaComponent;

		initSComponentPropertiesToolbar();
	}

	private void initSComponentPropertiesToolbar() {
		propertiesTable = new JTableX();
		SComponentPropertiesToolbarTableModel model = new SComponentPropertiesToolbarTableModel(
				TOOLBAR_TABLE_HEADER, schemaComponent.getParameters());

		// init panel
		setOpaque(true);

		// init table
		// look
		propertiesTable.setAutoResizeMode(TOOLBAR_RESIZE_MODE);
		propertiesTable.setRowSelectionAllowed(false);
		propertiesTable.setColumnSelectionAllowed(false);
		// model
		propertiesTable.setModel(model);
		initTableValues();

		// put table on panel
		add(propertiesTable, BorderLayout.CENTER);
	}

	private void initTableValues() {
		RowEditorModel rm = new RowEditorModel();
		propertiesTable.setRowEditorModel(rm);

		// FIXME smece,smece,smece
		JComboBox combo1 = new JComboBox(new String[] { "Prva vrijednost",
				"Druga", "Nonoe" });
		JComboBox combo2 = new JComboBox(new String[] { "combo2-1", "combo2-2",
				"itd..." });

		DefaultCellEditor cellEd1 = new DefaultCellEditor(combo1);
		rm.addEditorForRow(1, cellEd1);

		DefaultCellEditor cellEd2 = new DefaultCellEditor(combo2);
		rm.addEditorForRow(3, cellEd2);
	}
}
