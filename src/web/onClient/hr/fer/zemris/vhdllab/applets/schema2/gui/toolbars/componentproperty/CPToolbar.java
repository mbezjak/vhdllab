package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CPToolbar extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -6615541360994680172L;

	/**
	 * Sluzi za debug/retail mode ove komponente
	 */
	public static final boolean DEBUG_MODE = true;

	/**
	 * Ime stupaca tabele
	 */
	public static final String[] TOOLBAR_TABLE_HEADER = { "Name", "Value" };

	/**
	 * Ponasanje tabele
	 */
	public static final int TOOLBAR_RESIZE_MODE = JTable.AUTO_RESIZE_ALL_COLUMNS;

	/**
	 * Controller
	 */
	private ISchemaController controller = null;

	/**
	 * Referenca na lokalni kontroler za komunikaciju canvas<->toolbar
	 */
	private ILocalGuiController lgc = null;

	public CPToolbar(ILocalGuiController lgc, ISchemaController controller) {
		this.lgc = lgc;
		this.controller = controller;

		initGUI();
	}

	/**
	 * Inicijalizira graficki dio ovog toolbara
	 */
	private void initGUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(200, 100));
		setAlignmentX(LEFT_ALIGNMENT);
	}

	/**
	 * Registrirani listener
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		ISchemaComponentCollection componentCollection = null;
		ISchemaComponent component = null;
		ISchemaInfo info = null;
		Caseless componentName = null;

		componentName = (Caseless) evt.getNewValue();

		// dali je komponenta ili zica
		if (lgc.getSelectedType() == CanvasToolbarLocalGUIController.TYPE_WIRE) {
			if (DEBUG_MODE) {
				System.out.println("CPToolbar: selectedType=WIRE");
				System.out.println("CPToolbar: wireName=" + componentName);
			}

			showPropertyForWire(componentName);
		} else if (lgc.getSelectedType() == CanvasToolbarLocalGUIController.TYPE_COMPONENT) {
			if (DEBUG_MODE) {
				System.out.println("CPToolbar: selectedType=COMPONENT");
			}

			info = controller.getSchemaInfo();
			componentCollection = info.getComponents();
			component = componentCollection.fetchComponent(componentName);
			showPropertyForComponent(component);
		}
	}

	/**
	 * Graficki prikaz parametara za komponentu ZICA
	 * 
	 * @param componentName
	 *            ime zice
	 */
	private void showPropertyForWire(Caseless componentName) {
		cleanUpGui();
		printComponentName(componentName.toString());
	}

	/**
	 * Graficki prikaz za komponentu KOMPONENTA
	 * 
	 * @param component
	 *            komponenta koja se prikazuje
	 */
	public void showPropertyForComponent(ISchemaComponent component) {
		JTableX tabela = null;
		if (DEBUG_MODE) {
			String componentName = component.getName().toString();
			int numberOfParameters = component.getParameters().count();
			System.out.println("CPToolbar: componentName:" + componentName);
			System.out.println("CPToolbar: numberOfParameters:"
					+ numberOfParameters);
		}

		cleanUpGui();
		tabela = createJTableForComponent(component);
		printComponentName(component.getName().toString());
		faceLiftingForTable(tabela);
		printTable(tabela);
	}

	private void faceLiftingForTable(JTableX tabela) {
		tabela.setAutoResizeMode(TOOLBAR_RESIZE_MODE);
		tabela.setRowSelectionAllowed(false);
		tabela.setColumnSelectionAllowed(false);
	}

	private JTableX createJTableForComponent(ISchemaComponent component) {
		// izgenerirani envelope za sve parametre sa svim potrebnim vizualnim
		// komponentama
		CPToolbarParameterEnvelopeCollection pCollection = new CPToolbarParameterEnvelopeCollection(
				component);
		// na temelju envelopea, gradi se model za JTableX
		CPToolbarTableModel tableModel = new CPToolbarTableModel(pCollection);
		// na temelju envelopea, izgradio se i RowEditor model za JTableX
		RowEditorModel rowModel = pCollection.getRowEditorModel();

		JTableX tabela = new JTableX(tableModel, rowModel);

		return tabela;
	}

	private void printTable(JTableX tabela) {
		JScrollPane sPane = new JScrollPane(tabela);
		sPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(sPane);
		revalidate();
	}

	private void printComponentName(String componentName) {

		// dodaj labelu na panel
		JLabel labelComponentName = new JLabel("Selection: " + componentName);
		add(labelComponentName);
		revalidate();
	}

	/**
	 * Sluzi za ciscenje komponente
	 */
	private void cleanUpGui() {
		// makni sve komponente sa panela
		removeAll();
		revalidate();
	}
}
