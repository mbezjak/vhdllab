/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.JTableX;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.RowEditorModel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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

	/**
	 * Ime oznacene komponente
	 */
	private JLabel labelComponentName = new JLabel("", JLabel.CENTER);

	public CPToolbar(ILocalGuiController lgc, ISchemaController controller) {
		this.lgc = lgc;
		this.controller = controller;
		initGUI();
	}

	/**
	 * Inicijalizira graficki dio ovog toolbara
	 */
	private void initGUI() {
		setLayout(new BorderLayout());
		printWhenNothingSelected();
	}

	private void printWhenNothingSelected() {
		// labelComponentName.setText("Nothing selected");
		// add(labelComponentName, BorderLayout.NORTH);
		fetchAndShowEntityProperty();
	}

	/**
	 * Graficki prikaz parametara za komponentu ZICA
	 * 
	 * @param componentName
	 *            ime zice
	 */
	private void showPropertyForWire(ISchemaWire wire) {
		JTableX tabela = null;
		if (DEBUG_MODE) {
			String componentName = wire.getName().toString();
			int numberOfParameters = wire.getParameters().count();
			System.out.println("CPToolbar: componentName:" + componentName);
			System.out.println("CPToolbar: numberOfParameters:"
					+ numberOfParameters);
		}

		cleanUpGui();
		tabela = createJTableForWire(wire);
		printComponentName(wire.getName().toString());
		faceLiftingForTable(tabela);
		printTable(tabela);
		validate();
	}

	/**
	 * Graficki prikaz za komponentu KOMPONENTA
	 * 
	 * @param component
	 *            komponenta koja se prikazuje
	 */
	private void showPropertyForComponent(ISchemaComponent component) {
		if (component == null) {
			if (DEBUG_MODE) {
				System.err.println("CPToolbar: null component provided");
			}
			cleanUpGui();
			return;
		}
		JTableX tabela = null;
		String componentName = component.getName().toString();
		if (DEBUG_MODE) {
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
		validate();
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
				component, controller, lgc);
		// na temelju envelopea, gradi se model za JTableX
		CPToolbarTableModel tableModel = new CPToolbarTableModel(pCollection);
		// na temelju envelopea, izgradio se i RowEditor model za JTableX
		RowEditorModel rowModel = pCollection.getRowEditorModel();

		JTableX tabela = new JTableX(tableModel, rowModel);

		return tabela;
	}

	private JTableX createJTableForWire(ISchemaWire wire) {
		// izgenerirani envelope za sve parametre sa svim potrebnim vizualnim
		// komponentama
		CPToolbarParameterEnvelopeCollection pCollection = new CPToolbarParameterEnvelopeCollection(
				wire, controller, lgc);
		// na temelju envelopea, gradi se model za JTableX
		CPToolbarTableModel tableModel = new CPToolbarTableModel(pCollection);
		// na temelju envelopea, izgradio se i RowEditor model za JTableX
		RowEditorModel rowModel = pCollection.getRowEditorModel();

		JTableX tabela = new JTableX(tableModel, rowModel);

		return tabela;
	}

	private JTableX createJTabbleForEntity(ISchemaEntity entity) {
		// izgenerirani envelope za sve parametre sa svim potrebnim vizualnim
		// komponentama
		CPToolbarParameterEnvelopeCollection pCollection = new CPToolbarParameterEnvelopeCollection(
				entity, controller, lgc);
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
		add(sPane, BorderLayout.CENTER);

		tabela.setPreferredSize(null);
		setPreferredSize(tabela.getPreferredSize());
		sPane.invalidate();
	}

	private void printComponentName(String componentName) {
		labelComponentName.setText("Selection: " + componentName);
		labelComponentName.setOpaque(true);
		add(labelComponentName, BorderLayout.NORTH);
		labelComponentName.repaint();
		labelComponentName.invalidate();
	}

	/**
	 * Sluzi za ciscenje komponente
	 */
	private void cleanUpGui() {
		// makni sve komponente sa panela
		removeAll();
		repaint();
	}

	/**
	 * Registrirani listener
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_SELECTION)) {
			Caseless componentName = null;

			componentName = (Caseless) evt.getNewValue();

			// dali je komponenta ili zica
			if (isWire(lgc.getSelectedType())) {
				if (DEBUG_MODE) {
					System.out.println("CPToolbar: selectedType=WIRE");
					System.out.println("CPToolbar: wireName=" + componentName);
				}

				fetchAndShowWireProperty(componentName);
			} else if (isComponent(lgc.getSelectedType())) {
				if (DEBUG_MODE) {
					System.out.println("CPToolbar: selectedType=COMPONENT");
					System.out.println("CPToolbar: componentName="
							+ componentName);
				}

				fetchAndShowComponentProperty(componentName);
			} else if (lgc.getSelectedType() == CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED) {
				if (DEBUG_MODE) {
					System.out.println("CPToolbar: nothingSelected!");
				}
				cleanUpGui();
				printWhenNothingSelected();
			}
		} else if (evt.getPropertyName().equals(
				EPropertyChange.PROPERTY_CHANGE.toString())) {
			System.out.println("CPToolbar: selected component:"
					+ lgc.getSelectedComponent().toString());

			if (isWire(lgc.getSelectedType())) {
				fetchAndShowWireProperty(lgc.getSelectedComponent());
			} else if (isComponent(lgc.getSelectedType())) {
				fetchAndShowComponentProperty(lgc.getSelectedComponent());
			}
		} else {
			if (CPToolbar.DEBUG_MODE) {
				System.out.println("CPToolbar: unknow property");
			}
		}
	}

	private void fetchAndShowComponentProperty(Caseless componentName) {
		if (componentName == null)
			return;

		ISchemaInfo info = controller.getSchemaInfo();

		ISchemaComponentCollection componentCollection = info.getComponents();
		ISchemaComponent component = componentCollection
				.fetchComponent(componentName);
		showPropertyForComponent(component);
	}

	private void fetchAndShowWireProperty(Caseless componentName) {
		if (componentName == null)
			return;

		ISchemaInfo info = controller.getSchemaInfo();

		ISchemaWireCollection wiresCollection = info.getWires();
		ISchemaWire wire = wiresCollection.fetchWire(componentName);
		if (wire == null) {
			if (DEBUG_MODE) {
				System.err.println("CPToolbar: fetched wire was null");
			}
			return;
		}
		showPropertyForWire(wire);
	}

	private void fetchAndShowEntityProperty() {
		ISchemaInfo info = controller.getSchemaInfo();
		ISchemaEntity entity = info.getEntity();

		if (entity == null) {
			if (DEBUG_MODE) {
				System.err.println("CPToolbar: fetched entity was null!");
			}
			return;
		}
		showPropertyForEntity(entity);
	}

	private void showPropertyForEntity(ISchemaEntity entity) {
		JTableX tabela = null;
		String componentName = entity.getName().toString();

		if (DEBUG_MODE) {
			int numberOfParameters = entity.getParameters().count();
			System.out.println("CPToolbar: entityName:" + componentName);
			System.out.println("CPToolbar: numberOfParameters:"
					+ numberOfParameters);
		}

		tabela = createJTabbleForEntity(entity);
		printComponentName(componentName);
		faceLiftingForTable(tabela);
		printTable(tabela);
		validate();
	}

	private boolean isWire(int t) {
		if (t == CanvasToolbarLocalGUIController.TYPE_WIRE) {
			return true;
		}

		return false;
	}

	private boolean isComponent(int t) {
		if (t == CanvasToolbarLocalGUIController.TYPE_COMPONENT) {
			return true;
		}

		return false;
	}
}
