package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CPToolbar extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -6615541360994680172L;

	/**
	 * Sluzi za debug/retail mode ove komponente
	 */
	public static final boolean DEBUG_MODE = true;

	/**
	 * Controller
	 */
	private ISchemaController controller = null;

	/**
	 * Referenca na lokalni kontroler za komunikaciju canvas<->toolbar
	 */
	private ILocalGuiController lgc = null;

	/**
	 * Tabela za prikaz parametara
	 */
	private JTableX tabela = null;

	public CPToolbar(ILocalGuiController lgc, ISchemaController controller) {
		this.lgc = lgc;
		this.controller = controller;

		initGUI();
	}

	private void initGUI() {
		setLayout(new GridLayout(3, 1));
		setOpaque(true);
		setBackground(Color.green);
		setPreferredSize(new Dimension(200, 100));
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
	 * graficki prikaz parametara za komponentu ZICA
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
	 */
	public void showPropertyForComponent(ISchemaComponent component) {
		if (DEBUG_MODE) {
			String componentName = component.getName().toString();
			int numberOfParameters = component.getParameters().count();
			System.out.println("CPToolbar: componentName:" + componentName);
			System.out.println("CPToolbar: numberOfParameters:"
					+ numberOfParameters);
		}

		CPToolbarParameterEnvelopeCollection pCollection = new CPToolbarParameterEnvelopeCollection(
				component);

		cleanUpGui();
		createJTableForComponent(component);
		printComponentName(component.getName().toString());
	}

	private void createJTableForComponent(ISchemaComponent component) {
		
	}

	private void printComponentName(String componentName) {

		// dodaj labelu na panel
		JLabel labelComponentName = new JLabel("Selection: " + componentName,
				JLabel.LEFT);

		add(labelComponentName);
		revalidate();
	}

	/**
	 * Sluzi za ciscenje komponente
	 */
	private void cleanUpGui() {
		// makni sve komponente sa panela
		removeAll();
	}
}
